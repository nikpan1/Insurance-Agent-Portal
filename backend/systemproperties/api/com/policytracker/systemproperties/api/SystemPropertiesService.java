package com.policytracker.systemproperties.api;

import com.policytracker.common.architecture.DomainApi;
import com.policytracker.systemproperties.api.dto.SystemPropertyDto;
import java.util.Optional;

public interface SystemPropertiesService extends DomainApi {

    Optional<SystemPropertyDto> getProperty(Long customerId, String propertyKey);

    SystemPropertyDto upsertProperty(Long customerId, String propertyKey, String propertyValue);

    boolean getBooleanProperty(Long customerId, String propertyKey, boolean defaultValue);

    int getIntProperty(Long customerId, String propertyKey, int defaultValue);
}
