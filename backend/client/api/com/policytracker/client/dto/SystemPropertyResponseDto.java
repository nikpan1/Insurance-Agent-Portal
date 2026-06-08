package com.policytracker.client.dto;

public record SystemPropertyResponseDto(
        Long customerId,
        String propertyKey,
        String propertyValue,
        Long version
) {
}
