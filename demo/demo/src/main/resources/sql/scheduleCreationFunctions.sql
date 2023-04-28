CREATE OR REPLACE FUNCTION time_subtype_diff(x time, y time) RETURNS float8 AS
'SELECT EXTRACT(EPOCH FROM (x - y))' LANGUAGE sql STRICT IMMUTABLE;;

DO $$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'timerange') THEN
            CREATE TYPE timerange AS RANGE (
                                               subtype = time,
                                               subtype_diff = time_subtype_diff
                                           );
        END IF;
    END;
$$;;

DO $$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'timeslot') THEN
            CREATE TYPE timeslot AS (daysOfSlot TEXT, loc TEXT, start_time TIME, end_time TIME);
        END IF;
    END;
$$;;

DO $$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'fixedschedulecomponent') THEN
            CREATE TYPE fixedScheduleComponent AS ( crn VARCHAR, course TEXT, primaryIns VARCHAR);
        END IF;
    END;
$$;;


DO $$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'schedule') THEN
            CREATE TYPE schedule AS ( components schedulecomponent[], junk INTEGER);
        END IF;
    END;
$$;;


DO $$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'crnbucket') THEN
            CREATE TYPE crnBucket AS (crns VARCHAR[]);
        END IF;
    END;
$$;;

CREATE OR REPLACE FUNCTION bucketConcat(bucket crnBucket, newValue VARCHAR) RETURNS crnbucket AS $$
    BEGIN
        RETURN ROW(bucket.crns || newValue)::crnbucket;
    END;
$$ LANGUAGE plpgsql;;

CREATE OR REPLACE FUNCTION in_bucket(bucket crnbucket, value VARCHAR) RETURNS boolean AS $$
    BEGIN
        RETURN array_position(bucket.crns , value) IS NOT NULL;
    END;
$$ LANGUAGE plpgsql;;


CREATE OR REPLACE FUNCTION getSectionTimesOnDay(secCRN VARCHAR, onDay CHAR) RETURNS timemultirange AS $$
DECLARE
    dayLookupString VARCHAR := '%' || onDay || '%';
    builtRange timemultirange;
BEGIN
    SELECT range_agg(timerange(start_time, end_time)) INTO builtRange
        FROM (SELECT start_time, end_time, days FROM section_meetings sm JOIN meeting m ON sm.meetingid = m.meetingid AND sm.crn = secCRN) AS m
        WHERE (m.days LIKE dayLookupString);
    IF builtRange IS NULL THEN
        RETURN timemultirange();
    END IF;
    RETURN builtRange;
END;
$$ LANGUAGE plpgsql;;

CREATE OR REPLACE FUNCTION getSectionTimes(secCRN VARCHAR) RETURNS timemultirange[7] AS $$
BEGIN
    RETURN ARRAY[
        getSectionTimesOnDay(secCRN, 'M'),
        getSectionTimesOnDay(secCRN, 'T'),
        getSectionTimesOnDay(secCRN, 'W'),
        getSectionTimesOnDay(secCRN, 'R'),
        getSectionTimesOnDay(secCRN, 'F'),
        getSectionTimesOnDay(secCRN, 'S'),
        getSectionTimesOnDay(secCRN, 'U')
    ];
END;
$$ LANGUAGE plpgsql;;


ALTER TABLE section ADD COLUMN IF NOT EXISTS times timemultirange[7] DEFAULT NULL;;
BEGIN;
UPDATE section SET times = getSectionTimes(crn);;
COMMIT;


CREATE OR REPLACE FUNCTION anyTimesOverlap(tgtCRN VARCHAR, dstTimes timemultirange[7]) RETURNS BOOLEAN AS $$
    DECLARE tgtTimes timemultirange[7] := (SELECT times FROM section WHERE crn = tgtCRN);
    BEGIN
        RETURN (SELECT bool_or(tgt && dst) FROM unnest(tgtTimes, dstTimes) AS times(tgt, dst));
    END;
$$ LANGUAGE plpgsql;;

CREATE OR REPLACE FUNCTION unionOnAllDays(newCRN VARCHAR, prevTimes timemultirange[7]) RETURNS timemultirange[7] AS $$
    DECLARE newTimes timemultirange[7] := (SELECT times FROM section WHERE crn = newCRN);
    BEGIN
        RETURN ARRAY[
            newTimes[0] + prevTimes[0],
            newTimes[1] + prevTimes[1],
            newTimes[2] + prevTimes[2],
            newTimes[3] + prevTimes[3],
            newTimes[4] + prevTimes[4],
            newTimes[5] + prevTimes[5],
            newTimes[6] + prevTimes[6]
        ];
    END;
$$ LANGUAGE plpgsql;;

CREATE OR REPLACE FUNCTION spill_buckets(buckets crnBucket[]) RETURNS TABLE (individual VARCHAR) AS $$
    DECLARE
        bucket crnbucket;
        crns VARCHAR[];
    BEGIN
        FOREACH bucket IN ARRAY buckets LOOP
            crns := crns || bucket.crns;
        END LOOP;
        RETURN QUERY
            SELECT * FROM unnest(crns);
    END;
$$ LANGUAGE plpgsql;;

CREATE OR REPLACE FUNCTION bucket_subset(innerBucket crnBucket, outerBucket crnbucket) RETURNS boolean AS $$
    BEGIN
        RETURN innerBucket.crns <@ outerBucket.crns;
    END;
$$ LANGUAGE plpgsql;;

CREATE OR REPLACE FUNCTION extract_crns(bucket crnbucket) RETURNS VARCHAR[] AS $$
    BEGIN
        RETURN bucket.crns;
    END;
$$ LANGUAGE plpgsql;;

CREATE OR REPLACE FUNCTION retrieve_course(ofCrn VARCHAR) RETURNS TEXT AS $$
    BEGIN
        RETURN (SELECT target_subject || ' ' || target_course_number FROM section WHERE crn = ofCrn);
    END;
$$ LANGUAGE plpgsql;;

CREATE OR REPLACE FUNCTION generate_raw_schedules_simple(buckets crnBucket[])
    RETURNS setof VARCHAR[]
    AS $$
BEGIN
    RETURN QUERY
        WITH RECURSIVE accumulateSchedules(accCRNs, accTimes, accCourses) AS (
            SELECT ARRAY []::VARCHAR[], ARRAY_FILL(timemultirange(), ARRAY[7]), ARRAY []::TEXT[]
            UNION ALL
            (
                SELECT a.accCRNs || b.crn, unionOnAllDays(b.crn, a.accTimes), a.accCourses || retrieve_course(b.crn)
                FROM accumulateSchedules a, spill_buckets(buckets) AS b(crn)
                WHERE
                    NOT retrieve_course(b.crn) = ANY(a.accCourses) AND
                    NOT anyTimesOverlap(b.crn, a.accTimes)
            )
        )
        SELECT sch1.accCRNs

        FROM accumulateSchedules sch1 LEFT JOIN accumulateSchedules sch2
            ON (sch1.accCRNs <@ sch2.accCRNs) AND (NOT sch1.accCRNs @> sch2.accCRNs)
            WHERE sch2.accCRNs IS NULL;

END;
$$ LANGUAGE plpgsql;;

CREATE OR REPLACE FUNCTION getUnlockedCRNSet(tgtCrn VARCHAR) RETURNS crnbucket AS $$
    DECLARE tgtSub VARCHAR;
    DECLARE tgtCourseNumber VARCHAR;
    BEGIN
        SELECT target_subject, target_course_number INTO tgtSub, tgtCourseNumber FROM Section WHERE crn = tgtCrn;
        RETURN ROW((SELECT array_agg(crn) FROM Section WHERE tgtSub = target_subject AND tgtCourseNumber = target_course_number))::crnbucket;
    END;
$$ LANGUAGE plpgsql;;

CREATE OR REPLACE FUNCTION generate_raw_schedules_expanded(locked_crns VARCHAR[], unlocked_crns VARCHAR[])
    RETURNS setof VARCHAR[]
AS $$
    DECLARE
        unlocked_crn_sets crnbucket[];
        locked_crn_sets crnbucket[];
    BEGIN
        unlocked_crn_sets := ARRAY(
            SELECT getUnlockedCRNSet(crn) FROM unnest(unlocked_crns) AS u_crn(crn)
        );
        locked_crn_sets := ARRAY(
                SELECT ROW(ARRAY[crn])::crnbucket FROM unnest(locked_crns) AS l_crn(crn) WHERE NOT (crn =ANY(SELECT * FROM spill_buckets(unlocked_crn_sets)))
            );
        RETURN QUERY SELECT bucket FROM generate_raw_schedules_simple(unlocked_crn_sets || locked_crn_sets) AS buckets(bucket);
    END;
$$ LANGUAGE plpgsql;;

CREATE OR REPLACE FUNCTION buckets_test(locked_crns VARCHAR[], unlocked_crns VARCHAR[])
    RETURNS crnbucket[]
AS $$
DECLARE
    unlocked_crn_sets crnbucket[];
    locked_crn_sets crnbucket[];
BEGIN
    unlocked_crn_sets := ARRAY(
            SELECT getUnlockedCRNSet(crn) FROM unnest(unlocked_crns) AS u_crn(crn)
        );
    locked_crn_sets := ARRAY(
            SELECT ROW(ARRAY[crn])::crnbucket FROM unnest(locked_crns) AS l_crn(crn) WHERE NOT (crn =ANY(SELECT * FROM spill_buckets(unlocked_crn_sets)))
        );
    RETURN unlocked_crn_sets || locked_crn_sets;
END;
$$ LANGUAGE plpgsql;;


CREATE OR REPLACE FUNCTION timeslots_from_crn(schedCrn VARCHAR) RETURNS TABLE (days VARCHAR, room TEXT, startTime TIME, endTime TIME) AS $$
    BEGIN
        RETURN QUERY SELECT m.days, l.building || ' ' || l.room_number, m.start_time, m.end_time
            FROM section_meetings sm JOIN meeting m ON m.meetingid = sm.meetingid AND sm.crn = schedCrn JOIN location l ON l.locid = m.locid;
    END;
$$ LANGUAGE plpgsql;;


CREATE OR REPLACE FUNCTION fixed_schedule_component_from_crn(schedCrn VARCHAR) RETURNS TABLE (course TEXT, primaryIns VARCHAR)  AS $$
    BEGIN
        RETURN QUERY SELECT target_subject || ' ' || target_course_number, name FROM section WHERE crn = schedCrn;
    END;
$$ LANGUAGE plpgsql;;

CREATE OR REPLACE FUNCTION schedule_components_from_array(bucket varchar[]) RETURNS schedulecomponent[] AS $$
    BEGIN
        RETURN (
            WITH c(value) AS (SELECT unnest(bucket))
                SELECT array_agg(schedule_component_from_crn(value)) FROM c
        );
    END;
$$ LANGUAGE plpgsql;;

CREATE OR REPLACE FUNCTION lookup_schedule_components(locked_crns VARCHAR[], unlocked_crns VARCHAR[])
    RETURNS SETOF schedulecomponent[] AS $$
DECLARE
BEGIN
    RETURN QUERY
        SELECT schedule_components_from_array(scheduleBucket) FROM generate_raw_schedules_expanded(locked_crns, unlocked_crns) AS scheduleBucket;
END;
$$ LANGUAGE plpgsql;;

CREATE OR REPLACE FUNCTION comma_sep_crns(crns VARCHAR[]) RETURNS TEXT AS $$
    BEGIN
        RETURN (SELECT string_agg(val, ',') FROM unnest(crns) as collect(val));
    END;
$$ LANGUAGE plpgsql;;

CREATE OR REPLACE FUNCTION lookup_schedules(locked_crns VARCHAR[], unlocked_crns VARCHAR[])
    RETURNS setof TEXT AS $$
    DECLARE
    BEGIN
        RETURN QUERY
            SELECT comma_sep_crns(val) FROM generate_raw_schedules_expanded(locked_crns, unlocked_crns) as raw(val);
    END;
$$ LANGUAGE plpgsql;;
