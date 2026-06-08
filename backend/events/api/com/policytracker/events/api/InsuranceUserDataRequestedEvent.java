package com.policytracker.events.api;

import lombok.Builder;

@Builder
public record InsuranceUserDataRequestedEvent(
        Long userId,
        String externalUserId,
        String correlationId
) {
}
