package com.example.demo.database;

import com.example.demo.database.dtos.ScheduleLookupInputDTO;
import com.example.demo.database.dtos.ScheduleLookupOutputDTO;
import com.example.demo.database.dtos.SectionLookupInputDTO;
import com.example.demo.database.dtos.SectionLookupOutputDTO;
import com.example.demo.database.repositories.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class BSSRestController {

    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;
    private final LocationRepository locationRepository;
    private final SectionRepository sectionRepository;
    private final ScheduleRepository scheduleRepository;

    public BSSRestController(InstructorRepository instructorRepository, CourseRepository courseRepository, LocationRepository locationRepository, SectionRepository sectionRepository, ScheduleRepository scheduleRepository) {
        this.instructorRepository = instructorRepository;
        this.courseRepository = courseRepository;
        this.locationRepository = locationRepository;
        this.sectionRepository = sectionRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @GetMapping("/instructors")
    @ResponseBody
    public List<String> getInstructorNames() {
        return instructorRepository.findAllNames();
    }

    @GetMapping("/subjects")
    public List<String> getSubjects() {
        return courseRepository.findAllSubjectCodes();
    }

    @GetMapping("/departments")
    public List<String> getDepartments() {
        return courseRepository.findAllDepartments();
    }

    @GetMapping("/campuses")
    public List<String> getCampuses() {
        return locationRepository.getCampuses();
    }

    @GetMapping("/attributes")
    public List<String> getAttributes() {
        return courseRepository.findAllAttributes();
    }

    @GetMapping("/instructional-methods")
    public List<String> getInstructionalMethods() {
        return sectionRepository.getInstructionalMethods();
    }


    @PostMapping("/section-lookup")
    public List<SectionLookupOutputDTO> lookupSections(@RequestBody SectionLookupInputDTO secDTO) {

        Integer lowerCredBound = nullParseInt(secDTO.creditsLowBound());
        Integer higherCredBound = nullParseInt(secDTO.creditsHighBound());

        Integer startHour = nullParseInt(secDTO.startTimeHour());
        Integer startMinute = nullParseInt(secDTO.startTimeMinute());
        LocalTime startTime = nullParseTime(startHour, startMinute, secDTO.startTimeAMPM());

        Integer endHour = nullParseInt(secDTO.endTimeHour());
        Integer endMinute = nullParseInt(secDTO.endTimeMinute());
        LocalTime endTime = nullParseTime(endHour, endMinute, secDTO.endTimeAMPM());

        String days = computeDays(secDTO.mon(), secDTO.tue(), secDTO.wed(), secDTO.thu(), secDTO.fri(), secDTO.sat(), secDTO.sun());

        return sectionRepository.sectionSearch(
                emptyOnNull(secDTO.subjects()),
                secDTO.courseNumber(),
                secDTO.title(),
                emptyOnNull(secDTO.instructionalMethods()),
                lowerCredBound,
                higherCredBound,
                emptyOnNull(secDTO.departments()),
                emptyOnNull(secDTO.campuses()),
                emptyOnNull(secDTO.instructors()),
                emptyOnNull(secDTO.attributes()),
                startTime,
                endTime,
                days
        );
    }

    private Integer nullParseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // meridiem is what the M part of AM and PM is for in latin (translates to midday)`
    private LocalTime nullParseTime(Integer hour, Integer minute, String meridiem) {
        if (hour == null || minute == null )
            return null;

        if (hour < 0 || hour > 12 || minute < 0 || minute > 59)
            return null;

        if (!(Objects.equals(meridiem, "AM") || Objects.equals(meridiem, "PM")))
            return null;

        LocalTime time = LocalTime.of(hour, minute);

        if (meridiem.equals("PM"))
            return time.plusHours(12);

        return time;
    }

    private String computeDays(Boolean m, Boolean t, Boolean w, Boolean r, Boolean f, Boolean s, Boolean u) {
        if (m == null || t == null || w == null || r == null || f == null || s == null || u == null)
            return null;

        StringBuilder daysBuilder = new StringBuilder(7);

        if (m)
            daysBuilder.append('M');

        if (t)
            daysBuilder.append('T');

        if (w)
            daysBuilder.append('W');

        if (r)
            daysBuilder.append('R');

        if (f)
            daysBuilder.append('F');

        if (s)
            daysBuilder.append('S');

        if (u)
            daysBuilder.append('U');

        return daysBuilder.toString();
    }

    private String[] emptyOnNull(String[] in) {
        if (in != null)
            return in;
        return new String[0];
    }


    @PostMapping("/schedule-lookup")
    public List<List<ScheduleLookupOutputDTO>> lookupSections(@RequestBody ScheduleLookupInputDTO scheduleInputDTO) {
        return scheduleRepository.scheduleSearch(scheduleInputDTO.lockedCrns(), scheduleInputDTO.unlockedCrns());
    }

}
