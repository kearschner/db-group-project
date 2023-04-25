package com.example.demo.database.repositories;


import com.example.demo.data.Course;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {

    @Cacheable("subjectCache")
    @Query(value = "SELECT DISTINCT c.subject FROM Course c ORDER BY c.subject")
    public List<String> findAllSubjectCodes();

    @Cacheable("departmentCache")
    @Query(value = "SELECT DISTINCT c.department FROM Course c ORDER BY c.department")
    public List<String> findAllDepartments();

    @Cacheable("attributeCache")
    @Query(value = "SELECT DISTINCT c.attributes FROM Course c JOIN c.attributes")
    public List<String> findAllAttributes();
}
