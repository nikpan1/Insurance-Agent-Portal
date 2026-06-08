package com.policytracker.audit.core;

import com.policytracker.audit.api.AuditService;
import com.policytracker.audit.api.CreateAuditEventRequest;
import com.policytracker.common.events.ClientRegisteredEvent;
import com.policytracker.events.api.InsuranceStatusUpdatedEvent;
import com.policytracker.events.api.InsuranceUserDataRequestedEvent;
import com.policytracker.events.api.ReferenceDataImportedEvent;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuditDomainEventsListener {

    private final AuditService auditService;

    @EventListener
    public void onInsuranceStatusUpdated(InsuranceStatusUpdatedEvent event) {
        auditService.addAuditEvent(CreateAuditEventRequest.builder()
                .userId(event.userId())
                .eventType("INSURANCE_STATUS_UPDATED")
                .metadata(metadata(
                        "policyId", event.policyId(),
                        "status", event.status(),
                        "updatedAt", event.updatedAt() != null ? event.updatedAt().toString() : null
                ))
                .build());
    }

    @EventListener
    public void onInsuranceUserDataRequested(InsuranceUserDataRequestedEvent event) {
        auditService.addAuditEvent(CreateAuditEventRequest.builder()
                .userId(event.userId())
                .eventType("INSURANCE_USER_DATA_REQUESTED")
                .metadata(metadata(
                        "externalUserId", event.externalUserId(),
                        "correlationId", event.correlationId()
                ))
                .build());
    }

    @EventListener
    public void onReferenceDataImported(ReferenceDataImportedEvent event) {
        auditService.addAuditEvent(CreateAuditEventRequest.builder()
                .userId(event.userId())
                .eventType("REFERENCE_DATA_IMPORTED")
                .metadata(metadata(
                        "source", event.source(),
                        "importedRecords", String.valueOf(event.importedRecords()),
                        "importedAt", event.importedAt() != null ? event.importedAt().toString() : null
                ))
                .build());
    }

    @EventListener
    public void onClientRegistered(ClientRegisteredEvent event) {
        auditService.addAuditEvent(CreateAuditEventRequest.builder()
                .userId(event.clientId())
                .eventType("CLIENT_REGISTERED")
                .metadata(metadata("clientId", event.clientId() != null ? event.clientId().toString() : null))
                .build());
    }

    private Map<String, String> metadata(String... keyValues) {
        Map<String, String> metadata = new LinkedHashMap<>();
        for (int i = 0; i + 1 < keyValues.length; i += 2) {
            String key = keyValues[i];
            String value = keyValues[i + 1];
            if (value != null) {
                metadata.put(key, value);
            }
        }
        return metadata;
    }
}
