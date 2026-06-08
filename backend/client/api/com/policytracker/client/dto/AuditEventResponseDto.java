package com.policytracker.client.dto;

import java.time.Instant;
import java.util.Map;

public record AuditEventResponseDto(
        String id,
        Long userId,
        String eventType,
        Instant timestamp,
        Map<String, String> metadata
) {
}
