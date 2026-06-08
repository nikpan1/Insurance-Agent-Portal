package com.policytracker.audit.api.dto;

import java.util.Map;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateAuditEventRequest {
    Long userId;
    String eventType;
    Map<String, String> metadata;
}
