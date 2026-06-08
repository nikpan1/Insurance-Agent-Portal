package com.policytracker.systemproperties.core;

import com.policytracker.common.architecture.DomainCore;
import com.policytracker.systemproperties.api.SystemPropertiesService;
import com.policytracker.systemproperties.api.SystemPropertyValueFormatException;
import com.policytracker.systemproperties.api.dto.SystemPropertyDto;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SystemPropertiesServiceImpl implements SystemPropertiesService, DomainCore {

    private final SystemPropertyRepository systemPropertyRepository;
    private final SystemPropertyMapper systemPropertyMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<SystemPropertyDto> getProperty(Long customerId, String propertyKey) {
        return systemPropertyRepository.findByCustomerIdAndPropertyKey(customerId, propertyKey)
                .map(systemPropertyMapper::toDto);
    }

    @Override
    @Transactional
    public SystemPropertyDto upsertProperty(Long customerId, String propertyKey, String propertyValue) {
        SystemPropertyEntity entity = systemPropertyRepository
                .findByCustomerIdAndPropertyKey(customerId, propertyKey)
                .orElseGet(SystemPropertyEntity::new);

        entity.setCustomerId(customerId);
        entity.setPropertyKey(propertyKey);
        entity.setPropertyValue(propertyValue);

        SystemPropertyEntity persisted = systemPropertyRepository.save(entity);
        return systemPropertyMapper.toDto(persisted);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean getBooleanProperty(Long customerId, String propertyKey, boolean defaultValue) {
        Optional<SystemPropertyEntity> property = systemPropertyRepository.findByCustomerIdAndPropertyKey(customerId, propertyKey);

        if (property.isEmpty()) {
            return defaultValue;
        }

        String rawValue = property.get().getPropertyValue();
        if ("true".equalsIgnoreCase(rawValue) || "false".equalsIgnoreCase(rawValue)) {
            return Boolean.parseBoolean(rawValue);
        }

        throw new SystemPropertyValueFormatException(
                "Property '%s' for customer '%s' cannot be parsed as boolean".formatted(propertyKey, customerId)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public int getIntProperty(Long customerId, String propertyKey, int defaultValue) {
        Optional<SystemPropertyEntity> property = systemPropertyRepository.findByCustomerIdAndPropertyKey(customerId, propertyKey);

        if (property.isEmpty()) {
            return defaultValue;
        }

        String rawValue = property.get().getPropertyValue();
        try {
            return Integer.parseInt(rawValue);
        } catch (NumberFormatException ex) {
            throw new SystemPropertyValueFormatException(
                    "Property '%s' for customer '%s' cannot be parsed as integer".formatted(propertyKey, customerId)
            );
        }
    }
}
