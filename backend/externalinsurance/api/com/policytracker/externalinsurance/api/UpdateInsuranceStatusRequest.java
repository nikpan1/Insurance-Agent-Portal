package com.policytracker.externalinsurance.api;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UpdateInsuranceStatusRequest {
    String policyId;
    InsuranceStatus status;
    String reason;
    LocalDate effectiveDate;
}
