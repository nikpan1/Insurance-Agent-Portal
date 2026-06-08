package com.policytracker.client.dto;

import jakarta.validation.constraints.NotBlank;

public record ReferenceDataImportRequestDto(@NotBlank String sourcePath) {
}
