package com.policytracker.systemproperties.core;

import com.policytracker.systemproperties.api.SystemPropertyValueFormatException;
import com.policytracker.systemproperties.api.dto.SystemPropertyDto;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SystemPropertiesServiceImplTest {

    @Mock
    private SystemPropertyRepository systemPropertyRepository;

    @Mock
    private SystemPropertyMapper systemPropertyMapper;

    @InjectMocks
    private SystemPropertiesServiceImpl service;

    @Test
    void getBooleanPropertyReturnsDefaultWhenMissing() {
        when(systemPropertyRepository.findByCustomerIdAndPropertyKey(7L, "flag")).thenReturn(Optional.empty());

        assertThat(service.getBooleanProperty(7L, "flag", true)).isTrue();
    }

    @Test
    void getBooleanPropertyThrowsWhenValueIsInvalid() {
        SystemPropertyEntity entity = new SystemPropertyEntity();
        entity.setPropertyValue("not-bool");

        when(systemPropertyRepository.findByCustomerIdAndPropertyKey(7L, "flag")).thenReturn(Optional.of(entity));

        assertThatThrownBy(() -> service.getBooleanProperty(7L, "flag", false))
                .isInstanceOf(SystemPropertyValueFormatException.class);
    }

    @Test
    void getPropertyReturnsMappedDtoWhenFound() {
        SystemPropertyEntity entity = new SystemPropertyEntity();
        SystemPropertyDto dto = SystemPropertyDto.builder()
                .customerId(7L)
                .propertyKey("flag")
                .propertyValue("true")
                .version(1L)
                .build();

        when(systemPropertyRepository.findByCustomerIdAndPropertyKey(7L, "flag")).thenReturn(Optional.of(entity));
        when(systemPropertyMapper.toDto(entity)).thenReturn(dto);

        assertThat(service.getProperty(7L, "flag")).contains(dto);
    }

    @Test
    void upsertPropertyPersistsAndReturnsMappedDto() {
        SystemPropertyEntity entity = new SystemPropertyEntity();
        entity.setCustomerId(7L);
        entity.setPropertyKey("flag");
        entity.setPropertyValue("true");

        SystemPropertyDto dto = SystemPropertyDto.builder()
                .customerId(7L)
                .propertyKey("flag")
                .propertyValue("true")
                .version(1L)
                .build();

        when(systemPropertyRepository.findByCustomerIdAndPropertyKey(7L, "flag")).thenReturn(Optional.empty());
        when(systemPropertyRepository.save(any(SystemPropertyEntity.class))).thenReturn(entity);
        when(systemPropertyMapper.toDto(entity)).thenReturn(dto);

        assertThat(service.upsertProperty(7L, "flag", "true")).isEqualTo(dto);
        verify(systemPropertyRepository).save(any(SystemPropertyEntity.class));
    }

    @Test
    void getIntPropertyReturnsParsedValue() {
        SystemPropertyEntity entity = new SystemPropertyEntity();
        entity.setPropertyValue("42");

        when(systemPropertyRepository.findByCustomerIdAndPropertyKey(7L, "int-key")).thenReturn(Optional.of(entity));

        assertThat(service.getIntProperty(7L, "int-key", 5)).isEqualTo(42);
    }
}
