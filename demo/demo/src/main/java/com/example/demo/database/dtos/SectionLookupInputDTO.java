package com.example.demo.database.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SectionLookupInputDTO(
    @JsonProperty("subject[]")
    String[] subjects,

    @JsonProperty("course-number")
    String courseNumber,

    @JsonProperty("title")
    String title,

    @JsonProperty("instructional-method[]")
    String[] instructionalMethods,

    @JsonProperty("credit-range-min")
    String creditsLowBound,

    @JsonProperty("credit-range-max")
    String creditsHighBound,

    @JsonProperty("department[]")
    String[] departments,

    @JsonProperty("campus[]")
    String[] campuses,

    @JsonProperty("instructor[]")
    String[] instructors,

    @JsonProperty("attribute-type[]")
    String[] attributes,

    @JsonProperty("start-time-hour")
    String startTimeHour,

    @JsonProperty("start-time-minute")
    String startTimeMinute,

    @JsonProperty("start-time-am-pm")
    String startTimeAMPM,

    @JsonProperty("end-time-hour")
    String endTimeHour,

    @JsonProperty("end-time-minute")
    String endTimeMinute,

    @JsonProperty("end-time-am-pm")
    String endTimeAMPM,

    @JsonProperty("day-0")
    Boolean mon,
    @JsonProperty("day-1")
    Boolean tue,
    @JsonProperty("day-2")
    Boolean wed,
    @JsonProperty("day-3")
    Boolean thu,
    @JsonProperty("day-4")
    Boolean fri,
    @JsonProperty("day-5")
    Boolean sat,
    @JsonProperty("day-6")
    Boolean sun
    ) {}
