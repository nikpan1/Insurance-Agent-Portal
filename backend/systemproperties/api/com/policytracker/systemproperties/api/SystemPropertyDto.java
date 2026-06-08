package com.policytracker.systemproperties.api;

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
