package com.policytracker.referencedataimport.api;

import com.policytracker.common.architecture.DomainApi;
import com.policytracker.referencedataimport.api.dto.ReferenceDataImportResult;
import com.policytracker.referencedataimport.api.dto.ReferenceDataNode;
import java.util.List;

public interface ReferenceDataImportService extends DomainApi {

    ReferenceDataImportResult importFromCsv(String sourcePath);

    List<ReferenceDataNode> getCurrentTreeRoots();
}
