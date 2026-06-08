package com.policytracker.client.dto;

import com.policytracker.externalinsurance.api.InsuranceStatus;
import java.time.OffsetDateTime;

public record InsuranceStatusUpdateResultDto(
        String policyId,
        InsuranceStatus status,
        OffsetDateTime updatedAt
) {
}
