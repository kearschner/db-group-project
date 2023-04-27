package com.example.demo.database.repositories;

import com.example.demo.data.Section;
import com.example.demo.database.dtos.ScheduleLookupOutputDTO;
import com.example.demo.database.dtos.SectionLookupOutputDTO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Section, String>{
    @Query(value = "SELECT (generateSchedules(?1,?2)).*", nativeQuery = true)
    public List<List<ScheduleLookupOutputDTO>> scheduleSearch(String[] lockedCrns, String[] unlockedCrns);

}
