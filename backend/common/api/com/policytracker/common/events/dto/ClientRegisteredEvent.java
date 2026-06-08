package com.policytracker.common.events.dto;

import lombok.Builder;

@Builder
public record ClientRegisteredEvent(Long clientId) {
}
