package com.policytracker.client.dto;

import java.time.Instant;
import java.util.List;

public record ReferenceDataImportResultDto(
        String source,
        int importedRecords,
        Instant importedAt,
        List<ReferenceDataNodeDto> roots
) {
}
