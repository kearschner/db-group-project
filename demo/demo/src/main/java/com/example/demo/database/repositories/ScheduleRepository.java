package com.example.demo.database.repositories;

import com.example.demo.data.Section;
import com.example.demo.database.dtos.FixedScheduleComponentDTO;
import com.example.demo.database.dtos.ScheduleDTO;
import com.example.demo.database.dtos.TimeslotDTO;
import org.postgresql.core.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Section, String>{
    @Query(value = "SELECT lookup_schedules(?1,?2)", nativeQuery = true)
    public List<String> scheduleSearch(String[] lockedCrns, String[] unlockedCrns);

    @Query(value = "SELECT (fixed_schedule_component_from_crn(?1)).* LIMIT 1", nativeQuery = true)
    public FixedScheduleComponentDTO scheduleComponentSearch(String crn);

    @Query(value = "SELECT (timeslots_from_crn(?1)).*", nativeQuery = true)
    public List<TimeslotDTO>  timeslotSearch(String crn);
}
