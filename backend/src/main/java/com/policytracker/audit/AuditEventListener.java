package com.policytracker.audit;

import com.policytracker.common.events.ClientRegisteredEvent;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class AuditEventListener {

    private final AuditLogRepository auditLogRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onClientRegistered(ClientRegisteredEvent event) {
        AuditLog auditLog = new AuditLog();
        auditLog.setClientId(event.clientId());
        auditLog.setEventType("CLIENT_REGISTERED");
        auditLog.setCreatedAt(Instant.now());

        auditLogRepository.save(auditLog);
    }
}
