package com.policytracker.externalinsurance.core;

import com.policytracker.externalinsurance.api.dto.InsuranceUserData;
import com.policytracker.externalinsurance.api.dto.InsuranceUserDataRequest;
import com.policytracker.externalinsurance.api.dto.UpdateInsuranceStatusRequest;
import com.policytracker.externalinsurance.generated.model.InsuranceUserDataResponse;
import com.policytracker.externalinsurance.generated.model.UpdateInsuranceStatusResponse;
import com.policytracker.requestcontext.CurrentUserContext;
import com.policytracker.requestcontext.UserId;
import com.policytracker.systemproperties.api.SystemPropertiesService;
import java.time.LocalDate;
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
import static org.assertj.core.api.Assertions.assertThat;

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

    @Test
    void getInsuranceUserDataReturnsMappedValue() {
        InsuranceUserDataRequest request = InsuranceUserDataRequest.builder()
                .externalUserId("EXT-2")
                .correlationId("CORR-2")
                .build();

        InsuranceUserData mapped = InsuranceUserData.builder().externalUserId("EXT-2").build();

        when(currentUserContext.getUserId()).thenReturn(new UserId(10L));
        when(systemPropertiesService.getBooleanProperty(10L, "externalinsurance.events.enabled", true)).thenReturn(true);
        when(externalInsuranceMapper.toGeneratedInsuranceUserDataRequest(request))
                .thenReturn(new com.policytracker.externalinsurance.generated.model.InsuranceUserDataRequest());
        when(generatedExternalInsuranceClient.getInsuranceUserData(any())).thenReturn(new InsuranceUserDataResponse());
        when(externalInsuranceMapper.toApiInsuranceUserData(any())).thenReturn(mapped);

        InsuranceUserData result = externalInsuranceAdapter.getInsuranceUserData(request);

        assertThat(result.getExternalUserId()).isEqualTo("EXT-2");
    }

    @Test
    void updateInsuranceStatusReturnsMappedValue() {
        UpdateInsuranceStatusRequest request = UpdateInsuranceStatusRequest.builder()
                .policyId("POL-1")
                .status(com.policytracker.externalinsurance.api.dto.InsuranceStatus.ACTIVE)
                .reason("reason")
                .effectiveDate(LocalDate.parse("2026-06-09"))
                .build();

        com.policytracker.externalinsurance.api.dto.InsuranceStatusUpdateResult mapped =
                com.policytracker.externalinsurance.api.dto.InsuranceStatusUpdateResult.builder()
                        .policyId("POL-1")
                        .status(com.policytracker.externalinsurance.api.dto.InsuranceStatus.ACTIVE)
                        .build();

        when(currentUserContext.getUserId()).thenReturn(new UserId(10L));
        when(systemPropertiesService.getBooleanProperty(10L, "externalinsurance.events.enabled", true)).thenReturn(true);
        when(externalInsuranceMapper.toGeneratedUpdateInsuranceStatusRequest(request))
                .thenReturn(new com.policytracker.externalinsurance.generated.model.UpdateInsuranceStatusRequest());
        when(generatedExternalInsuranceClient.updateInsuranceStatus(any())).thenReturn(new UpdateInsuranceStatusResponse());
        when(externalInsuranceMapper.toApiInsuranceStatusUpdateResult(any())).thenReturn(mapped);

        com.policytracker.externalinsurance.api.dto.InsuranceStatusUpdateResult result =
                externalInsuranceAdapter.updateInsuranceStatus(request);

        assertThat(result.getPolicyId()).isEqualTo("POL-1");
    }
}
