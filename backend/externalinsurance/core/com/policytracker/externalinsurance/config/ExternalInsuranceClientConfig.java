package com.policytracker.externalinsurance.config;

import com.policytracker.externalinsurance.core.GeneratedExternalInsuranceClient;
import com.policytracker.externalinsurance.core.OpenApiExternalInsuranceClient;
import com.policytracker.externalinsurance.generated.api.DefaultApi;
import com.policytracker.externalinsurance.generated.invoker.ApiClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ExternalInsuranceProperties.class)
public class ExternalInsuranceClientConfig {

    @Bean
    public ApiClient externalInsuranceApiClient(
            final ExternalInsuranceProperties properties,
            final RestTemplateBuilder restTemplateBuilder
    ) {
        final ApiClient apiClient = new ApiClient(restTemplateBuilder.build());
        apiClient.setBasePath(properties.getBaseUrl());
        return apiClient;
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultApi externalInsuranceDefaultApi(final ApiClient externalInsuranceApiClient) {
        return new DefaultApi(externalInsuranceApiClient);
    }

    @Bean
    @ConditionalOnMissingBean(GeneratedExternalInsuranceClient.class)
    public GeneratedExternalInsuranceClient generatedExternalInsuranceClient(final DefaultApi defaultApi) {
        return new OpenApiExternalInsuranceClient(defaultApi);
    }
}
