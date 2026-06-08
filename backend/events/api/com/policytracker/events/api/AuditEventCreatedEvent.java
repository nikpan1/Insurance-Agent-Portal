package com.policytracker.events.api;

import java.time.Instant;
import lombok.Builder;

@Builder
public record AuditEventCreatedEvent(
        Long userId,
        String eventType,
        Instant timestamp
) {
}
