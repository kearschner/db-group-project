package com.example.demo.database.repositories;

import com.example.demo.data.Section;
import com.example.demo.database.dtos.SectionLookupOutputDTO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface SectionRepository extends JpaRepository<Section, String>{
    @Query(value = "SELECT (lookupSections(?1,?2,?3,?4,?5,?6,?7,?8,?9,?10,?11,?12,?13)).*", nativeQuery = true)
    public List<SectionLookupOutputDTO> sectionSearch(
            String[] subjects,
            String courseNumber,
            String title,
            String[] instructMethods,
            Integer lowCreditBound,
            Integer highCreditBound,
            String[] departments,
            String[] campuses,
            String[] instructors,
            String[] attributes,
            LocalTime startTime,
            LocalTime endTime,
            String days);

    @Cacheable("methodCache")
    @Query(value = "SELECT DISTINCT s.instructMethod FROM Section s WHERE (NOT s.instructMethod = ' ') ORDER BY s.instructMethod")
    public List<String> getInstructionalMethods();
}
