package com.policytracker.audit.core;

import java.time.Instant;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("audit_events")
@Getter
@Setter
@NoArgsConstructor
public class AuditEventDocument {

    @Id
    private String id;

    private Long userId;

    private String eventType;

    private Instant timestamp;

    private Map<String, String> metadata;
}
