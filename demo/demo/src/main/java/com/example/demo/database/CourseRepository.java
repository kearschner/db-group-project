package com.example.demo.database;


import com.example.demo.data.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {

    @Query(value = "SELECT DISTINCT c.department FROM Course c ORDER BY c.department")
    public List<String> findAllDepartments();
}
