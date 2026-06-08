package com.policytracker.events.api.dto;

import lombok.Builder;

@Builder
public record InsuranceUserDataRequestedEvent(
        Long userId,
        String externalUserId,
        String correlationId
) {
}
