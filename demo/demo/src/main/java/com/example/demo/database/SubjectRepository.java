package com.example.demo.database;

import com.example.demo.data.Instructor;
import com.example.demo.data.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, String> {

    @Query(value = "SELECT s.subjectCode FROM Subject s ORDER BY s.subjectCode")
    public List<String> findAllSubjectCodes();
}
