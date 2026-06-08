package com.policytracker.client.dto;

import jakarta.validation.constraints.NotBlank;

public record SystemPropertyUpsertRequestDto(@NotBlank String propertyValue) {
}
