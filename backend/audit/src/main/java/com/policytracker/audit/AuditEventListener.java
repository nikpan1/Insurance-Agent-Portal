package com.policytracker.audit;

import com.policytracker.common.events.ClientRegisteredEvent;
import com.policytracker.events.api.AuditEventCreatedEvent;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class AuditEventListener {

    private final AuditLogRepository auditLogRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onClientRegistered(ClientRegisteredEvent event) {
        AuditLog auditLog = new AuditLog();
        auditLog.setClientId(event.clientId());
        auditLog.setEventType("CLIENT_REGISTERED");
        auditLog.setCreatedAt(Instant.now());

        AuditLog savedAuditLog = auditLogRepository.save(auditLog);
        eventPublisher.publishEvent(AuditEventCreatedEvent.builder()
                .userId(savedAuditLog.getClientId())
                .eventType(savedAuditLog.getEventType())
                .timestamp(savedAuditLog.getCreatedAt())
                .build());
    }
}
