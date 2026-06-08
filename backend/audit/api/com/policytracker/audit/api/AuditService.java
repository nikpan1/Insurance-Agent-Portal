package com.policytracker.audit.api;

import com.policytracker.audit.api.dto.AuditEventDto;
import com.policytracker.audit.api.dto.CreateAuditEventRequest;
import com.policytracker.common.architecture.DomainApi;
import java.util.List;

public interface AuditService extends DomainApi {

    AuditEventDto addAuditEvent(CreateAuditEventRequest request);

    List<AuditEventDto> getAuditEventsByUserId(Long userId);
}
