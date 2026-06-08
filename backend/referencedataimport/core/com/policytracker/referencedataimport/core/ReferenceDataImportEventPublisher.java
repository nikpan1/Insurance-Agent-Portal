package com.policytracker.referencedataimport.core;

import com.policytracker.common.architecture.DomainCore;
import com.policytracker.events.api.ReferenceDataImportedEvent;
import com.policytracker.referencedataimport.api.ReferenceDataImportService;
import com.policytracker.requestcontext.CurrentUserContext;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReferenceDataImportEventPublisher implements ReferenceDataImportService, DomainCore {

    private final CurrentUserContext currentUserContext;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void publishImportCompleted(String source, int importedRecords) {
        Long userId = currentUserContext.getUserId() != null
                ? currentUserContext.getUserId().value()
                : null;

        eventPublisher.publishEvent(ReferenceDataImportedEvent.builder()
                .userId(userId)
                .source(source)
                .importedRecords(importedRecords)
                .importedAt(Instant.now())
                .build());
    }
}
