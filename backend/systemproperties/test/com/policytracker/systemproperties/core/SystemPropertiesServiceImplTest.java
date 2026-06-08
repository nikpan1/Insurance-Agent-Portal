package com.policytracker.systemproperties.core;

import com.policytracker.systemproperties.api.SystemPropertyValueFormatException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
}
