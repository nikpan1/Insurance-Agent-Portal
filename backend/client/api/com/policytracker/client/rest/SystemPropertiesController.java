package com.policytracker.client.rest;

import com.policytracker.client.dto.SystemPropertyResponseDto;
import com.policytracker.client.dto.SystemPropertyUpsertRequestDto;
import com.policytracker.systemproperties.api.SystemPropertiesService;
import com.policytracker.systemproperties.api.SystemPropertyDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/system-properties")
@RequiredArgsConstructor
public class SystemPropertiesController {

    private final SystemPropertiesService systemPropertiesService;

    @GetMapping("/{customerId}/{propertyKey}")
    public ResponseEntity<SystemPropertyResponseDto> getProperty(
            @PathVariable Long customerId,
            @PathVariable String propertyKey
    ) {
        return systemPropertiesService.getProperty(customerId, propertyKey)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{customerId}/{propertyKey}")
    public SystemPropertyResponseDto upsertProperty(
            @PathVariable Long customerId,
            @PathVariable String propertyKey,
            @Valid @RequestBody SystemPropertyUpsertRequestDto request
    ) {
        SystemPropertyDto propertyDto = systemPropertiesService.upsertProperty(customerId, propertyKey, request.propertyValue());
        return toResponse(propertyDto);
    }

    private SystemPropertyResponseDto toResponse(SystemPropertyDto dto) {
        return new SystemPropertyResponseDto(dto.getCustomerId(), dto.getPropertyKey(), dto.getPropertyValue(), dto.getVersion());
    }
}
