package com.example.demo.database.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ScheduleLookupInputDTO (
        @JsonProperty("locked")
        String[] locked,

        @JsonProperty("unlocked")
        String[] unlocked
)
{ }
