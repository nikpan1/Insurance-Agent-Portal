package com.policytracker.client.dto;

import com.policytracker.externalinsurance.api.dto.InsuranceStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record UpdateInsuranceStatusRequestDto(
        @NotNull InsuranceStatus status,
        @NotBlank String reason,
        @NotNull LocalDate effectiveDate
) {
}
