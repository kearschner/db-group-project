package com.example.demo.database;

import com.example.demo.data.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, String> {

    @Query(value = "SELECT ins.name FROM Instructor ins ORDER BY ins.name")
    public List<String> findAllNames();

}
