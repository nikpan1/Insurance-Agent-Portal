package com.policytracker.events.api;

import java.time.OffsetDateTime;
import lombok.Builder;

@Builder
public record InsuranceStatusUpdatedEvent(
        Long userId,
        String policyId,
        String status,
        OffsetDateTime updatedAt
) {
}
