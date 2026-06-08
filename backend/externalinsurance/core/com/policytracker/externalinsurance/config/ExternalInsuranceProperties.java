package com.policytracker.externalinsurance.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "external-insurance")
public class ExternalInsuranceProperties {

    @NotBlank
    private String baseUrl = "https://api.external-insurance.com/v1";
}
