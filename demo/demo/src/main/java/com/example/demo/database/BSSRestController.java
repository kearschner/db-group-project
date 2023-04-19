package com.example.demo.database;

import com.example.demo.data.Section;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BSSRestController {

    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;
    private final LocationRepository locationRepository;

    public BSSRestController(InstructorRepository instructorRepository, CourseRepository courseRepository, LocationRepository locationRepository) {
        this.instructorRepository = instructorRepository;
        this.courseRepository = courseRepository;
        this.locationRepository = locationRepository;
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

    @PostMapping("/section-lookup")
    public List<Section> lookupSections(@RequestBody SectionLookupDTO secDTO) {
        System.out.println(secDTO.attributes());

        return null;
    }


}
