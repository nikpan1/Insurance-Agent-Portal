package com.policytracker.audit;

import com.policytracker.common.events.ClientRegisteredEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuditEventListenerTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private AuditEventListener auditEventListener;

    @Test
    void onClientRegisteredSavesAuditLog() {
        ClientRegisteredEvent event = ClientRegisteredEvent.builder()
                .clientId(42L)
                .build();
        when(auditLogRepository.save(any(AuditLog.class))).thenAnswer(invocation -> invocation.getArgument(0));

        auditEventListener.onClientRegistered(event);

        ArgumentCaptor<AuditLog> captor = ArgumentCaptor.forClass(AuditLog.class);
        verify(auditLogRepository, times(1)).save(captor.capture());

        AuditLog savedAuditLog = captor.getValue();
        assertThat(savedAuditLog.getClientId()).isEqualTo(42L);
        assertThat(savedAuditLog.getEventType()).isEqualTo("CLIENT_REGISTERED");
        assertThat(savedAuditLog.getCreatedAt()).isNotNull();
    }
}
