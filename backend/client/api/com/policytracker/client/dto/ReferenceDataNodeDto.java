package com.policytracker.client.dto;

import java.util.List;

public record ReferenceDataNodeDto(
        String id,
        String parentId,
        String name,
        List<ReferenceDataNodeDto> children
) {
}
