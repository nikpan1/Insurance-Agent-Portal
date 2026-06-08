package com.policytracker.client.rest;

import com.policytracker.audit.api.AuditService;
import com.policytracker.audit.api.dto.AuditEventDto;
import com.policytracker.audit.api.dto.CreateAuditEventRequest;
import com.policytracker.client.dto.AuditEventResponseDto;
import com.policytracker.client.dto.CreateAuditEventRequestDto;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/audit-events")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;

    @PostMapping
    public AuditEventResponseDto addAuditEvent(@Valid @RequestBody CreateAuditEventRequestDto request) {
        AuditEventDto auditEventDto = auditService.addAuditEvent(CreateAuditEventRequest.builder()
                .userId(request.userId())
                .eventType(request.eventType())
                .metadata(request.metadata())
                .build());
        return toResponse(auditEventDto);
    }

    @GetMapping("/users/{userId}")
    public List<AuditEventResponseDto> getAuditEvents(@PathVariable Long userId) {
        return auditService.getAuditEventsByUserId(userId).stream().map(this::toResponse).toList();
    }

    private AuditEventResponseDto toResponse(AuditEventDto dto) {
        return new AuditEventResponseDto(dto.getId(), dto.getUserId(), dto.getEventType(), dto.getTimestamp(), dto.getMetadata());
    }
}
