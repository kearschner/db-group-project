package com.example.demo.database;

import com.example.demo.data.Meeting;

public interface MeetingRepositoryCustom {

    public Meeting insertMeetingSafe(Meeting meeting);
}
