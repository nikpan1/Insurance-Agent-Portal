package com.policytracker.externalinsurance.api;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class InsuranceUserData {
    String externalUserId;
    String firstName;
    String lastName;
    List<InsurancePolicy> activePolicies;
    Float riskScore;
}
