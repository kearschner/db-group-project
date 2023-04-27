package com.example.demo.database.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalTime;

public interface TimeslotDTO {

    @JsonProperty("daysOfSlot")
    String getDaysOfSlot();

    @JsonProperty("loc")
    String getLoc();

    @JsonProperty("start-time")
    LocalTime getStartTime();

    @JsonProperty("end-time")
    LocalTime getEndTime();
}
