package com.policytracker.audit.api.dto;

import java.time.Instant;
import java.util.Map;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AuditEventDto {
    String id;
    Long userId;
    String eventType;
    Instant timestamp;
    Map<String, String> metadata;
}
