package com.example.demo.database.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface SectionLookupOutputDTO {

    @JsonProperty("crn")
    String getCrn();

    @JsonProperty("subject")
    String getSubject();

    @JsonProperty("course_number")
    String getCourseNumber();

    @JsonProperty("section_number")
    String getSectionNumber();

    @JsonProperty("credits")
    Integer getCredits();

    @JsonProperty("title")
    String getTitle();

    @JsonProperty("instructional_method")
    String getInstructionalMethod();

    @JsonProperty("permit_req")
    Boolean getPermitReq();

    @JsonProperty("term_dates")
    String getTermDates();

    @JsonProperty("days")
    String getDays();

    @JsonProperty("times")
    String getTimes();

    @JsonProperty("instructor")
    String getInstructor();

    @JsonProperty("campus")
    String getCampus();

    @JsonProperty("location")
    String getLocation();

    @JsonProperty("attribute_type")
    String getAttributes();

    @JsonProperty("department")
    String getDepartment();

}
