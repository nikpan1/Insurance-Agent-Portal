package com.policytracker.systemproperties.core;

import com.policytracker.systemproperties.api.dto.SystemPropertyDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface SystemPropertyMapper {

    @Mapping(target = "customerId", source = "customerId")
    @Mapping(target = "propertyKey", source = "propertyKey")
    @Mapping(target = "propertyValue", source = "propertyValue")
    @Mapping(target = "version", source = "version")
    SystemPropertyDto toDto(SystemPropertyEntity entity);
}
