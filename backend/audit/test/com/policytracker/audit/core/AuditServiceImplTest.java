package com.policytracker.audit.core;

import com.policytracker.audit.api.CreateAuditEventRequest;
import com.policytracker.events.api.AuditEventCreatedEvent;
import java.time.Instant;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
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
class AuditServiceImplTest {

    @Mock
    private AuditEventRepository auditEventRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private AuditServiceImpl auditService;

    @Test
    void addAuditEventPersistsDocumentAndPublishesEvent() {
        AuditEventMapper mapper = Mappers.getMapper(AuditEventMapper.class);
        auditService = new AuditServiceImpl(auditEventRepository, mapper, eventPublisher);

        CreateAuditEventRequest request = CreateAuditEventRequest.builder()
                .userId(77L)
                .eventType("TEST_EVENT")
                .metadata(Map.of("k", "v"))
                .build();

        when(auditEventRepository.save(any(AuditEventDocument.class))).thenAnswer(invocation -> {
            AuditEventDocument document = invocation.getArgument(0);
            document.setId("evt-1");
            if (document.getTimestamp() == null) {
                document.setTimestamp(Instant.now());
            }
            return document;
        });

        auditService.addAuditEvent(request);

        ArgumentCaptor<AuditEventDocument> documentCaptor = ArgumentCaptor.forClass(AuditEventDocument.class);
        verify(auditEventRepository, times(1)).save(documentCaptor.capture());
        assertThat(documentCaptor.getValue().getUserId()).isEqualTo(77L);
        assertThat(documentCaptor.getValue().getEventType()).isEqualTo("TEST_EVENT");

        ArgumentCaptor<AuditEventCreatedEvent> eventCaptor = ArgumentCaptor.forClass(AuditEventCreatedEvent.class);
        verify(eventPublisher, times(1)).publishEvent(eventCaptor.capture());
        assertThat(eventCaptor.getValue().userId()).isEqualTo(77L);
        assertThat(eventCaptor.getValue().eventType()).isEqualTo("TEST_EVENT");
        assertThat(eventCaptor.getValue().timestamp()).isNotNull();
    }
}
