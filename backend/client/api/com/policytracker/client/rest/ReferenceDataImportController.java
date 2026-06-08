package com.policytracker.client.rest;

import com.policytracker.client.dto.ReferenceDataImportRequestDto;
import com.policytracker.client.dto.ReferenceDataImportResultDto;
import com.policytracker.client.dto.ReferenceDataNodeDto;
import com.policytracker.referencedataimport.api.ReferenceDataImportResult;
import com.policytracker.referencedataimport.api.ReferenceDataImportService;
import com.policytracker.referencedataimport.api.ReferenceDataNode;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reference-data")
@RequiredArgsConstructor
public class ReferenceDataImportController {

    private final ReferenceDataImportService referenceDataImportService;

    @PostMapping("/import")
    public ReferenceDataImportResultDto importFromCsv(@Valid @RequestBody ReferenceDataImportRequestDto request) {
        ReferenceDataImportResult importResult = referenceDataImportService.importFromCsv(request.sourcePath());
        return toResponse(importResult);
    }

    @GetMapping("/tree")
    public List<ReferenceDataNodeDto> getCurrentTreeRoots() {
        return referenceDataImportService.getCurrentTreeRoots().stream().map(this::toNodeDto).toList();
    }

    private ReferenceDataImportResultDto toResponse(ReferenceDataImportResult result) {
        return new ReferenceDataImportResultDto(
                result.source(),
                result.importedRecords(),
                result.importedAt(),
                result.roots().stream().map(this::toNodeDto).toList()
        );
    }

    private ReferenceDataNodeDto toNodeDto(ReferenceDataNode node) {
        return new ReferenceDataNodeDto(
                node.id(),
                node.parentId(),
                node.name(),
                node.children().stream().map(this::toNodeDto).toList()
        );
    }
}
