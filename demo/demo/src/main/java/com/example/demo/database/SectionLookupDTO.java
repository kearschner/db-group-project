package com.example.demo.database;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SectionLookupDTO (
    @JsonProperty("subject[]")
    List<String> subjects,

    @JsonProperty("course-number")
    String courseNumber,

    @JsonProperty("instructional-method[]")
    List<String> instructionalMethods,

    @JsonProperty("credits-range-min")
    String creditsLowBound,

    @JsonProperty("credits-range-max")
    String creditsHighBound,

    @JsonProperty("department[]")
    List<String> departments,

    @JsonProperty("campus[]")
    List<String> campuses,

    @JsonProperty("instructor[]")
    List<String> instructors,

    @JsonProperty("attribute-type[]")
    List<String> attributes,

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
    String mon,
    @JsonProperty("day-1")
    String tue,
    @JsonProperty("day-2")
    String wed,
    @JsonProperty("day-3")
    String thu,
    @JsonProperty("day-4")
    String fri,
    @JsonProperty("day-5")
    String sat,
    @JsonProperty("day-6")
    String sun
    ) {}
