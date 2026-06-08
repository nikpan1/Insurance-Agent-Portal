package com.policytracker.client.dto;

import com.policytracker.externalinsurance.api.dto.InsuranceStatus;
import java.time.OffsetDateTime;

public record InsuranceStatusUpdateResultDto(
        String policyId,
        InsuranceStatus status,
        OffsetDateTime updatedAt
) {
}
