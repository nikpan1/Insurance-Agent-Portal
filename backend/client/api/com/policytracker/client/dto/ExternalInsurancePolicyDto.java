package com.policytracker.client.dto;

import com.policytracker.externalinsurance.api.InsuranceStatus;

public record ExternalInsurancePolicyDto(
        String policyId,
        String type,
        InsuranceStatus status
) {
}
