package com.policytracker.systemproperties.api.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SystemPropertyDto {
    Long customerId;
    String propertyKey;
    String propertyValue;
    Long version;
}
