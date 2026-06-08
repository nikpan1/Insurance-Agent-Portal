package com.policytracker.audit.core;

import com.policytracker.audit.api.dto.AuditEventDto;
import com.policytracker.audit.api.dto.CreateAuditEventRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AuditEventMapper {

    AuditEventDto toDto(AuditEventDocument document);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timestamp", expression = "java(java.time.Instant.now())")
    AuditEventDocument toDocument(CreateAuditEventRequest request);
}
