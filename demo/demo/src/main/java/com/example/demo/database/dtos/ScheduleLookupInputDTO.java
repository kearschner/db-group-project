package com.example.demo.database.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ScheduleLookupInputDTO (
        @JsonProperty("locked-crns[]")
        String[] lockedCrns,

        @JsonProperty("unlocked-crns[]")
        String[] unlockedCrns
)
{ }
