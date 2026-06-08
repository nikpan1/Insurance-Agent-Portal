package com.policytracker.events.api.dto;

import java.time.Instant;
import lombok.Builder;

@Builder
public record ReferenceDataImportedEvent(
        Long userId,
        String source,
        int importedRecords,
        Instant importedAt
) {
}
