package com.policytracker.externalinsurance.api.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class InsurancePolicy {
    String policyId;
    String type;
    InsuranceStatus status;
}
