package com.policytracker.externalinsurance.api.dto;

import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class InsuranceStatusUpdateResult {
    String policyId;
    InsuranceStatus status;
    OffsetDateTime updatedAt;
}
