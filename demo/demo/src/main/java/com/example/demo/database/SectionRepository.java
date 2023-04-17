package com.example.demo.database;

import com.example.demo.data.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionRepository extends JpaRepository<Section, String> {

    @Query(value = "SELECT s FROM Section s JOIN Course c")
    public List<Section> sectionSearch();

}
