package com.example.demo.database.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface ScheduleLookupOutputDTO {

    @JsonProperty("crn")
    String getCrn();

    @JsonProperty("course")
    String getCourse();

    @JsonProperty("slots[]")
    TimeslotDTO getSlots();

    @JsonProperty("primary-ins")
    String getPrimaryIns();

}
