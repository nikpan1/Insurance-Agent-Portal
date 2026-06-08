package com.policytracker.common.events;

import lombok.Builder;

@Builder
public record ClientRegisteredEvent(Long clientId) {
}
