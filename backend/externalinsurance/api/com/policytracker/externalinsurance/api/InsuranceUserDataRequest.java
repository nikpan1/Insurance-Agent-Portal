package com.policytracker.externalinsurance.api;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class InsuranceUserDataRequest {
    String externalUserId;
    String correlationId;
}
