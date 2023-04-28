package com.example.demo.database.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ScheduleLookupInputDTO (
        @JsonProperty("crns[]")
        String[] lockedCrns,

        @JsonProperty("unlocked_crns[]")
        String[] unlockedCrns
)
{ }
