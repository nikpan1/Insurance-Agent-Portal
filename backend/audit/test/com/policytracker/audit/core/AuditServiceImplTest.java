package com.policytracker.audit.core;

import com.policytracker.audit.api.dto.CreateAuditEventRequest;
import java.util.List;
import com.policytracker.events.api.dto.AuditEventCreatedEvent;
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

    @Test
    void getAuditEventsByUserIdReturnsMappedEvents() {
        AuditEventMapper mapper = Mappers.getMapper(AuditEventMapper.class);
        auditService = new AuditServiceImpl(auditEventRepository, mapper, eventPublisher);

        AuditEventDocument first = new AuditEventDocument();
        first.setId("evt-1");
        first.setUserId(77L);
        first.setEventType("FIRST");
        first.setTimestamp(Instant.parse("2026-06-09T00:00:00Z"));

        AuditEventDocument second = new AuditEventDocument();
        second.setId("evt-2");
        second.setUserId(77L);
        second.setEventType("SECOND");
        second.setTimestamp(Instant.parse("2026-06-09T00:01:00Z"));

        when(auditEventRepository.findByUserIdOrderByTimestampDesc(77L)).thenReturn(List.of(first, second));

        assertThat(auditService.getAuditEventsByUserId(77L))
                .extracting("id")
                .containsExactly("evt-1", "evt-2");
    }
}
