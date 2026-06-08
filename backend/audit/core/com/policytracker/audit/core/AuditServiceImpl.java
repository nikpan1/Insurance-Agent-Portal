package com.policytracker.audit.core;

import com.policytracker.audit.api.AuditService;
import com.policytracker.audit.api.dto.AuditEventDto;
import com.policytracker.audit.api.dto.CreateAuditEventRequest;
import com.policytracker.common.architecture.DomainCore;
import com.policytracker.events.api.dto.AuditEventCreatedEvent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService, DomainCore {

    private final AuditEventRepository auditEventRepository;
    private final AuditEventMapper auditEventMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public AuditEventDto addAuditEvent(CreateAuditEventRequest request) {
        AuditEventDocument document = auditEventMapper.toDocument(request);
        AuditEventDocument saved = auditEventRepository.save(document);

        eventPublisher.publishEvent(AuditEventCreatedEvent.builder()
                .userId(saved.getUserId())
                .eventType(saved.getEventType())
                .timestamp(saved.getTimestamp())
                .build());

        return auditEventMapper.toDto(saved);
    }

    @Override
    public List<AuditEventDto> getAuditEventsByUserId(Long userId) {
        return auditEventRepository.findByUserIdOrderByTimestampDesc(userId)
                .stream()
                .map(auditEventMapper::toDto)
                .toList();
    }
}
