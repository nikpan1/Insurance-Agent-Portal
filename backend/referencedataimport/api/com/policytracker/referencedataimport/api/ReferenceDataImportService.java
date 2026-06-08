package com.policytracker.referencedataimport.api;

import com.policytracker.common.architecture.DomainApi;

public interface ReferenceDataImportService extends DomainApi {

    void publishImportCompleted(String source, int importedRecords);
}
