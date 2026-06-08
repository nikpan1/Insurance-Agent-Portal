package com.policytracker.referencedataimport.api;

import java.time.Instant;
import java.util.List;

public record ReferenceDataImportResult(
        String source,
        int importedRecords,
        Instant importedAt,
        List<ReferenceDataNode> roots
) {
}
