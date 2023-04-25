package com.example.demo.database.repositories;

import com.example.demo.data.Meeting;

public interface MeetingRepositoryCustom {

    public Meeting insertMeetingSafe(Meeting meeting);
}
