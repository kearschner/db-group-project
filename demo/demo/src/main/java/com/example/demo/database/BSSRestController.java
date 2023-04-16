package com.example.demo.database;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BSSRestController {

    private final InstructorRepository instructorRepository;
    private final SubjectRepository subjectRepository;
    private final CourseRepository courseRepository;
    private final LocationRepository locationRepository;

    public BSSRestController(InstructorRepository instructorRepository, SubjectRepository subjectRepository, CourseRepository courseRepository, LocationRepository locationRepository) {
        this.instructorRepository = instructorRepository;
        this.subjectRepository = subjectRepository;
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
        return subjectRepository.findAllSubjectCodes();
        //return subjectRepository.findAll(Sort.by(Sort.Direction.ASC, "subjectCode"));
    }

    @GetMapping("/departments")
    public List<String> getDepartments() {
        return courseRepository.findAllDepartments();
    }

    @GetMapping("/campuses")
    public List<String> getCampuses() {
        return locationRepository.getCampuses();
    }

}
