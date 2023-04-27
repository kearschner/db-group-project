CREATE OR REPLACE FUNCTION compositeInstructors(secCRN VARCHAR, primaryInstructor VARCHAR) RETURNS TEXT AS $$
DECLARE additional TEXT := (
    SELECT STRING_AGG(i.name, ', ') FROM Section_All_Instructors i WHERE i.crn = secCRN AND i.name != primaryInstructor
);
BEGIN
    IF additional IS NULL THEN
        RETURN primaryInstructor || ' (P)';
    ELSE
        RETURN primaryInstructor || ' (P), ' || additional;
    END IF;
END;
$$  LANGUAGE plpgsql;;

CREATE OR REPLACE FUNCTION compositeAttributes(subject VARCHAR, course_number VARCHAR) RETURNS TEXT AS $$
BEGIN
    RETURN (
        SELECT STRING_AGG(a.attributes, ' and ') FROM course_attributes a
        WHERE a.course_course_number = course_number AND a.course_subject = subject
    );
END;
$$  LANGUAGE plpgsql;;

CREATE OR REPLACE  FUNCTION compositeDates(startDate DATE, endDate DATE) RETURNS TEXT AS $$
BEGIN
    RETURN TO_CHAR(startDate, 'MM/DD') || '-' || TO_CHAR(endDate, 'MM/DD');
END;
$$ LANGUAGE plpgsql;;

CREATE OR REPLACE  FUNCTION compositeTimes(startTime TIME, endTime TIME) RETURNS TEXT AS $$
BEGIN
    RETURN TO_CHAR(startTime, 'HH12:MI AM') || '-' || TO_CHAR(endTime, 'HH12:MI AM');
END;
$$ LANGUAGE plpgsql;;

CREATE OR REPLACE FUNCTION coalesceDays(targetCRN VARCHAR, startTime TIME, endTime TIME) RETURNS TEXT AS $$
BEGIN
    RETURN (
        SELECT STRING_AGG(m.days, NULL)
        FROM Meeting m JOIN Section_Meetings sm ON
            sm.meetingid = m.meetingid AND
            (startTime IS NULL OR m.start_time = startTime) AND
            (endTime IS NULL OR m.end_time = endTime)
        WHERE sm.crn = targetCRN);
END;
$$ LANGUAGE plpgsql;;

CREATE OR REPLACE FUNCTION lookupSections(
    TEXT[],
    Course.course_number%TYPE,
    Course.title%TYPE,
    TEXT[],
    Course.credits%TYPE,
    Course.credits%TYPE,
    TEXT[],
    TEXT[],
    TEXT[],
    TEXT[],
    Meeting.start_time%TYPE,
    Meeting.end_time%TYPE,
    Meeting.days%TYPE
) RETURNS TABLE (crn VARCHAR(255),
                 subject VARCHAR(255),
                 courseNumber VARCHAR(255),
                 sectionNumber VARCHAR(255),
                 credits INTEGER,
                 title VARCHAR(255),
                 instructionalMethod VARCHAR(255),
                 permitRequired BOOL,
                 termDates TEXT,
                 days VARCHAR(255),
                 times TEXT,
                 instructor TEXT,
                 campus VARCHAR(255),
                 loc TEXT,
                 attribs TEXT,
                 department VARCHAR(255)) AS $$
BEGIN
    RETURN QUERY
        SELECT filtSec.crn,
               filtCourse.subject,
               filtCourse.course_number,
               filtSec.section_number,
               filtCourse.credits,
               filtCourse.title,
               filtSec.instructional_method,
               filtCourse.permit_required,
               compositeDates(filtSec.start_date, filtSec.end_date),
               m.days,
               compositeTimes(m.start_time, m.end_time),
               compositeInstructors(filtSec.crn, filtSec.name),
               l.campus,
               l.building || ' ' || l.room_number,
               compositeAttributes(filtCourse.subject, filtCourse.course_number),
               filtCourse.department
        FROM (
            SELECT *
            FROM Section s
            WHERE (array_length($9,1) IS NULL OR s.crn IN (SELECT i.crn FROM Section_All_Instructors i WHERE i.name = ANY($9)))
                AND ($11 IS NULL OR STRING_TO_ARRAY($13, NULL) && STRING_TO_ARRAY(coalesceDays(s.crn, $11, $12), NULL))
            ) AS filtSec
            JOIN
            (SELECT *
              FROM Course c
              WHERE (c.subject, c.course_number) IN
                    (SELECT a.course_subject, a.course_course_number
                     FROM Course_Attributes a
                     WHERE (array_length($10,1) IS NULL OR a.attributes = ANY($10)))) AS filtCourse
             ON filtSec.target_course_number = filtCourse.course_number AND filtSec.target_subject = filtCourse.subject
                 NATURAL JOIN section_meetings
                 NATURAL JOIN Meeting m
                 NATURAL JOIN Location l
        WHERE (array_length($1,1) IS NULL OR filtCourse.subject = ANY($1))
          AND ($2 IS NULL OR filtCourse.course_number = $2)
          AND ($3 IS NULL OR filtCourse.title = $3)
          AND (array_length($4,1) IS NULL OR filtSec.instructional_method = ANY($4))
          AND ($5 IS NULL OR $6 IS NULL OR int4range($5, $6, '[]') @> filtCourse.credits)
          AND (array_length($7,1) IS NULL OR filtCourse.department = ANY($7))
          AND (array_length($8,1) IS NULL OR l.campus = ANY($8));
    RETURN;
END;
$$  LANGUAGE plpgsql;;