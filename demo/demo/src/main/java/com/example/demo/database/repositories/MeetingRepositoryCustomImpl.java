package com.example.demo.database.repositories;

import com.example.demo.data.Meeting;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;

public class MeetingRepositoryCustomImpl implements MeetingRepositoryCustom{

    @Autowired
    private EntityManager entityManager;

    @Override
    public Meeting insertMeetingSafe(Meeting meeting) {
        Query ins = entityManager.createNativeQuery("INSERT INTO Meeting (days, start_time, end_time, locid) VALUES (?1,?2,?3, ?4) ON CONFLICT DO NOTHING RETURNING *",
                Meeting.class);
        ins.setParameter(1, meeting.days());
        ins.setParameter(2,meeting.startTime());
        ins.setParameter(3, meeting.endTime());
        ins.setParameter(4, meeting.location().id());
        var results = ins.getResultList();

        if (!results.isEmpty())
            return (Meeting) results.get(0);

        Query oldLookup = entityManager.createNativeQuery("SELECT * from Meeting WHERE days = ?1 AND start_time = ?2 AND end_time = ?3 AND locid = ?4 LIMIT 1", Meeting.class);
        oldLookup.setParameter(1, meeting.days());
        oldLookup.setParameter(2,meeting.startTime());
        oldLookup.setParameter(3, meeting.endTime());
        oldLookup.setParameter(4, meeting.location().id());
        return (Meeting) oldLookup.getSingleResult();
    }

}
