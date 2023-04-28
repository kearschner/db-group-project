package com.example.demo.database;

import com.example.demo.database.dtos.FixedScheduleComponentDTO;
import com.example.demo.database.dtos.ScheduleComponentDTO;
import com.example.demo.database.dtos.ScheduleDTO;
import com.example.demo.database.repositories.ScheduleRepository;
import org.postgresql.util.PGobject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    private ScheduleComponentDTO componentFromCrnString(String crn) {
        FixedScheduleComponentDTO fixedPortion = scheduleRepository.scheduleComponentSearch(crn);
        return new ScheduleComponentDTO(crn,fixedPortion.getCourse(), scheduleRepository.timeslotSearch(crn), fixedPortion.getPrimaryIns());
    }

    private ScheduleDTO dtoFromSet(Set<String> crns) {
        List<ScheduleComponentDTO> dtos = new ArrayList<>();

        for (String crn : crns) {
            dtos.add(componentFromCrnString(crn));
        }

        return new ScheduleDTO(dtos);
    }

    public List<ScheduleDTO> getSchedules(String[] locked, String[] unlocked) {

        List<String> commaSepCrns = scheduleRepository.scheduleSearch(locked, unlocked);
        if (commaSepCrns == null)
            return null;
        List<Set<String>> crnSets = new ArrayList<>();
        List<ScheduleDTO> dtos = new ArrayList<>();
        for (String commaSep : commaSepCrns) {
            if (commaSep == null)
                continue;
            String[] crns = commaSep.split(",");
            Set<String> potentialNewSet = Set.of(crns);
            boolean hasRepeated = false;
            for (Set<String> preChecked : crnSets) {
                if (preChecked.equals(potentialNewSet)) {
                    hasRepeated = true;
                    break;
                }
            }
            if (!hasRepeated)
                crnSets.add(potentialNewSet);
        }

        for (Set<String> crnSet : crnSets) {
            dtos.add(dtoFromSet(crnSet));
        }

        return dtos;
    }



}
