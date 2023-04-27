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

CREATE OR REPLACE FUNCTION getSectionTimesOnDay(secCRN VARCHAR, onDay CHAR) RETURNS timemultirange AS $$
DECLARE dayLookupString VARCHAR(3) := '%' || onDay || '%';
BEGIN
    RETURN (
        SELECT range_agg(timerange(start_time, end_time))
        FROM (SELECT start_time, end_time, days FROM section_meetings sm JOIN meeting m ON sm.meetingid = m.meetingid AND sm.crn = secCRN) AS m
        WHERE (dayLookupString LIKE m.days));
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
UPDATE section SET times = getSectionTimes(crn);;


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

CREATE OR REPLACE FUNCTION generateRawSchedulesClean(crnBuckets VARCHAR[][])
    RETURNS VARCHAR[][]
    AS $$
BEGIN
    RETURN ARRAY(
            WITH RECURSIVE accumulateSchedules(accCRNs, accTimes) AS (
                SELECT ARRAY [], ARRAY_FILL(timemultirange(), 7)
                UNION ALL
                (
                    WITH avail_crns AS (
                        SELECT UNNEST(ARRAY(SELECT UNNEST(bucket)
                                            FROM crnBuckets bucket
                                            WHERE NOT (bucket && (
                                                SELECT accCRNs
                                                FROM accumulateSchedules)
                                                ))) AS crn
                    )
                    SELECT "aS".accCRNs || "aC".crn, unionOnAllDays("aC".crn, "aS".accTimes)
                    FROM accumulateSchedules "aS",
                         avail_crns "aC"
                    WHERE NOT anyTimesOverlap("aC".crn, "aS".accTimes)
                )
            )
            SELECT sch1.accCRNs
            FROM accumulateSchedules sch1
                     LEFT JOIN accumulateSchedules sch2
                               ON sch1.accCRNs <@ sch2.accCRNs
            WHERE sch2.accCRNs IS NULL
    );
END;
$$ LANGUAGE plpgsql;;

CREATE OR REPLACE FUNCTION getUnlockedCRNSet(tgtCrn VARCHAR) RETURNS VARCHAR[] AS $$
    DECLARE tgtSub VARCHAR;
    DECLARE tgtCourseNumber VARCHAR;
    BEGIN
        SELECT target_subject, target_course_number INTO tgtSub, tgtCourseNumber FROM Section WHERE crn = tgtCrn;
        RETURN (SELECT array_agg(crn) FROM Section WHERE tgtSub = target_subject AND tgtCourseNumber = target_course_number);
    END;
$$ LANGUAGE plpgsql;;

CREATE OR REPLACE FUNCTION generateRawSchedulesExpanded(crns VARCHAR[], unlocked_crns VARCHAR[])
    RETURNS VARCHAR[][]
AS $$
    DECLARE
        unlocked_crn_sets VARCHAR[][] := ARRAY(
            SELECT DISTINCT getUnlockedCRNSet(u_crn.crn) FROM unnest(unlocked_crns) AS u_crn(crn)
            );
        locked_crn_sets VARCHAR[][] := ARRAY(
            SELECT ARRAY[crn] FROM crns crn WHERE NOT (crn = ANY(SELECT unnest(array(SELECT unnest(u_crn) FROM unlocked_crn_sets u_crn))))
            );
    BEGIN
        RETURN generateRawSchedulesClean(unlocked_crn_sets + locked_crn_sets);
    END;
$$ LANGUAGE plpgsql;;

DO $$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'timeslot') THEN
            CREATE TYPE timeslot AS (daysOfSlot TEXT, loc TEXT, start_time TIME, end_time TIME);
        END IF;
    END;
$$;;

DO $$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'schedulecomponent') THEN
            CREATE TYPE scheduleComponent AS ( crn VARCHAR, course TEXT, slots timeslot[], primaryIns VARCHAR);
        END IF;
    END;
$$;;

CREATE OR REPLACE FUNCTION timeslots_from_crn(schedCrn VARCHAR) RETURNS timeslot[] AS $$
    BEGIN
        RETURN ARRAY(SELECT ROW(m.days, m.start_time, m.end_time, l.building || ' ' || l.room_number)::timeslot
            FROM section_meetings sm JOIN meeting m ON m.meetingid = sm.meetingid AND sm.crn = schedCrn JOIN location l ON l.locid = m.locid);
    END;
$$ LANGUAGE plpgsql;;


CREATE OR REPLACE FUNCTION schedule_component_from_crn(schedCrn VARCHAR) RETURNS scheduleComponent AS $$
    DECLARE
        course TEXT;
        primaryIns VARCHAR;
    BEGIN
        SELECT target_subject || ' ' || target_course_number, name INTO course, primaryIns FROM section WHERE crn = schedCrn LIMIT 1;
        RETURN ROW(schedCrn, course, timeslots_from_crn(schedCrn), primaryIns)::scheduleComponent;
    END;
$$ LANGUAGE plpgsql;;

CREATE OR REPLACE FUNCTION generateSchedules(crns VARCHAR[], unlocked_crns VARCHAR[])
    RETURNS scheduleComponent[][] AS $$
    DECLARE
        rawSchedulues VARCHAR[][] := generateRawSchedulesExpanded(crns, unlocked_crns);
        allSchedules scheduleComponent[][] := ARRAY [Array []];
        localComponents scheduleComponent[];
        scheduleSet VARCHAR[];
        localCrn VARCHAR;
    BEGIN
        FOREACH scheduleSet IN ARRAY rawSchedulues
        LOOP
            localComponents := ARRAY [];
            FOREACH localCrn IN ARRAY scheduleSet
            LOOP
                localComponents := localComponents + schedule_component_from_crn(localCrn);
            END LOOP;
            allSchedules := allSchedules + localComponents;
        END LOOP;
        RETURN allSchedules;
    END;
$$ LANGUAGE plpgsql;;
