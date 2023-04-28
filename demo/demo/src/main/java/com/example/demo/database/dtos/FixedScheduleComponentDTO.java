package com.example.demo.database.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;

public interface FixedScheduleComponentDTO {

    @JsonProperty("course")
    String getCourse();

    @JsonProperty("primaryIns")
    String getPrimaryIns();

}
