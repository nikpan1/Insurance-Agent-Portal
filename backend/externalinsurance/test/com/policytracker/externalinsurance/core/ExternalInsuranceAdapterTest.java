package com.policytracker.externalinsurance.core;

import com.policytracker.externalinsurance.api.InsuranceUserData;
import com.policytracker.externalinsurance.api.InsuranceUserDataRequest;
import com.policytracker.externalinsurance.generated.model.InsuranceUserDataResponse;
import com.policytracker.requestcontext.CurrentUserContext;
import com.policytracker.requestcontext.UserId;
import com.policytracker.systemproperties.api.SystemPropertiesService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalInsuranceAdapterTest {

    @Mock
    private GeneratedExternalInsuranceClient generatedExternalInsuranceClient;

    @Mock
    private ExternalInsuranceMapper externalInsuranceMapper;

    @Mock
    private CurrentUserContext currentUserContext;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private SystemPropertiesService systemPropertiesService;

    @InjectMocks
    private ExternalInsuranceAdapter externalInsuranceAdapter;

    @Test
    void getInsuranceUserDataDoesNotPublishEventWhenDisabled() {
        InsuranceUserDataRequest request = InsuranceUserDataRequest.builder()
                .externalUserId("EXT-1")
                .correlationId("CORR-1")
                .build();

        when(currentUserContext.getUserId()).thenReturn(new UserId(10L));
        when(systemPropertiesService.getBooleanProperty(10L, "externalinsurance.events.enabled", true)).thenReturn(false);
        when(externalInsuranceMapper.toGeneratedInsuranceUserDataRequest(request))
                .thenReturn(new com.policytracker.externalinsurance.generated.model.InsuranceUserDataRequest());
        when(generatedExternalInsuranceClient.getInsuranceUserData(any())).thenReturn(new InsuranceUserDataResponse());
        when(externalInsuranceMapper.toApiInsuranceUserData(any())).thenReturn(InsuranceUserData.builder().build());

        externalInsuranceAdapter.getInsuranceUserData(request);

        verify(eventPublisher, never()).publishEvent(any());
    }
}
