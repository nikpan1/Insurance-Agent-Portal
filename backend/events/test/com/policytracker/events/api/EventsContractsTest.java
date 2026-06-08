package com.policytracker.events.api;

import java.time.Instant;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EventsContractsTest {

    @Test
    void buildersCreateEventContracts() {
        InsuranceStatusUpdatedEvent statusUpdated = InsuranceStatusUpdatedEvent.builder()
                .userId(1L)
                .policyId("P-1")
                .status("ACTIVE")
                .updatedAt(OffsetDateTime.parse("2026-06-09T00:00:00Z"))
                .build();

        InsuranceUserDataRequestedEvent userDataRequested = InsuranceUserDataRequestedEvent.builder()
                .userId(1L)
                .externalUserId("EXT-1")
                .correlationId("CORR-1")
                .build();

        AuditEventCreatedEvent auditEvent = AuditEventCreatedEvent.builder()
                .userId(1L)
                .eventType("TEST")
                .timestamp(Instant.parse("2026-06-09T00:00:00Z"))
                .build();

        ReferenceDataImportedEvent importedEvent = ReferenceDataImportedEvent.builder()
                .userId(1L)
                .source("/tmp/ref.csv")
                .importedRecords(3)
                .importedAt(Instant.parse("2026-06-09T00:00:00Z"))
                .build();

        assertThat(statusUpdated.policyId()).isEqualTo("P-1");
        assertThat(userDataRequested.externalUserId()).isEqualTo("EXT-1");
        assertThat(auditEvent.eventType()).isEqualTo("TEST");
        assertThat(importedEvent.importedRecords()).isEqualTo(3);
    }
}
