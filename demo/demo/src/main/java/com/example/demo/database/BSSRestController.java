package com.example.demo.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BSSRestController {

    private final InstructorRepository instructorRepository;

    public BSSRestController(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    @GetMapping("/instructors")
    @ResponseBody
    public List<String> getInstructorNames() {
        return instructorRepository.findAllNames();
    }
}
