package com.policytracker.client.dto;

import java.util.List;

public record ExternalInsuranceUserDataResponseDto(
        String externalUserId,
        String firstName,
        String lastName,
        List<ExternalInsurancePolicyDto> activePolicies,
        Float riskScore
) {
}
