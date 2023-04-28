package com.example.demo.database.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalTime;

public interface TimeslotDTO {

    @JsonProperty("room")
    String getRoom();

    @JsonProperty("startTime")
    LocalTime getStartTime();

    @JsonProperty("days")
    String getDays();

    @JsonProperty("endTime")
    LocalTime getEndTime();
}
