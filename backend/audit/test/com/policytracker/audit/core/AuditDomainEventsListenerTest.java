package com.policytracker.audit.core;

import com.policytracker.audit.api.AuditService;
import com.policytracker.audit.api.dto.CreateAuditEventRequest;
import com.policytracker.events.api.dto.InsuranceStatusUpdatedEvent;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuditDomainEventsListenerTest {

    @Mock
    private AuditService auditService;

    @InjectMocks
    private AuditDomainEventsListener listener;

    @Test
    void onInsuranceStatusUpdatedCreatesAuditEvent() {
        InsuranceStatusUpdatedEvent event = InsuranceStatusUpdatedEvent.builder()
                .userId(11L)
                .policyId("POL-42")
                .status("ACTIVE")
                .updatedAt(OffsetDateTime.parse("2026-06-09T00:00:00Z"))
                .build();

        listener.onInsuranceStatusUpdated(event);

        ArgumentCaptor<CreateAuditEventRequest> captor = ArgumentCaptor.forClass(CreateAuditEventRequest.class);
        verify(auditService, times(1)).addAuditEvent(captor.capture());
        CreateAuditEventRequest request = captor.getValue();
        assertThat(request.getUserId()).isEqualTo(11L);
        assertThat(request.getEventType()).isEqualTo("INSURANCE_STATUS_UPDATED");
        assertThat(request.getMetadata()).containsEntry("policyId", "POL-42");
        assertThat(request.getMetadata()).containsEntry("status", "ACTIVE");
    }
}
