package com.example.demo.database.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ScheduleDTO (

    @JsonProperty("components")
    List<ScheduleComponentDTO> components

) { }
