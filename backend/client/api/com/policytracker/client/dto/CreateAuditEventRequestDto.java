package com.policytracker.client.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

public record CreateAuditEventRequestDto(
        @NotNull Long userId,
        @NotBlank String eventType,
        Map<String, String> metadata
) {
}
