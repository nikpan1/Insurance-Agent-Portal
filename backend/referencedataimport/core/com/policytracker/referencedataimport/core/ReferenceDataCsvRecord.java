package com.policytracker.referencedataimport.core;

record ReferenceDataCsvRecord(
        int lineNumber,
        String id,
        String parentId,
        String name
) {
}
