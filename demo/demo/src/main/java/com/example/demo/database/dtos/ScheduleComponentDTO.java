package com.example.demo.database.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ScheduleComponentDTO (

    @JsonProperty("crn")
    String crn,

    @JsonProperty("course")
    String course,

    @JsonProperty("slots")
    List<TimeslotDTO> slots,

    @JsonProperty("primaryIns")
    String primaryIns

) {}
