package com.policytracker.externalinsurance.core;

import com.policytracker.common.architecture.DomainCore;
import com.policytracker.externalinsurance.api.ExternalInsuranceCommunicationException;
import com.policytracker.externalinsurance.api.ExternalInsuranceService;
import com.policytracker.externalinsurance.api.InsuranceStatusUpdateResult;
import com.policytracker.externalinsurance.api.InsuranceUserData;
import com.policytracker.externalinsurance.api.InsuranceUserDataRequest;
import com.policytracker.externalinsurance.api.UpdateInsuranceStatusRequest;
import com.policytracker.events.api.InsuranceStatusUpdatedEvent;
import com.policytracker.events.api.InsuranceUserDataRequestedEvent;
import com.policytracker.requestcontext.CurrentUserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

@Service
@RequiredArgsConstructor
public class ExternalInsuranceAdapter implements ExternalInsuranceService, DomainCore {

    private final GeneratedExternalInsuranceClient generatedExternalInsuranceClient;
    private final ExternalInsuranceMapper externalInsuranceMapper;
    private final CurrentUserContext currentUserContext;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public InsuranceUserData getInsuranceUserData(final InsuranceUserDataRequest request) {
        final Long userId = currentUserContext.getUserId() != null
                ? currentUserContext.getUserId().value()
                : null;

        try {
            final com.policytracker.externalinsurance.generated.model.InsuranceUserDataRequest generatedRequest =
                    externalInsuranceMapper.toGeneratedInsuranceUserDataRequest(request);
            final com.policytracker.externalinsurance.generated.model.InsuranceUserDataResponse generatedResponse =
                    generatedExternalInsuranceClient.getInsuranceUserData(generatedRequest);
            final InsuranceUserData insuranceUserData = externalInsuranceMapper.toApiInsuranceUserData(generatedResponse);
            eventPublisher.publishEvent(InsuranceUserDataRequestedEvent.builder()
                    .userId(userId)
                    .externalUserId(request.getExternalUserId())
                    .correlationId(request.getCorrelationId())
                    .build());
            return insuranceUserData;
        } catch (RestClientException ex) {
            throw new ExternalInsuranceCommunicationException("Failed to fetch insurance user data from external provider", ex);
        }
    }

    @Override
    public InsuranceStatusUpdateResult updateInsuranceStatus(final UpdateInsuranceStatusRequest request) {
        final Long userId = currentUserContext.getUserId() != null
                ? currentUserContext.getUserId().value()
                : null;

        try {
            final com.policytracker.externalinsurance.generated.model.UpdateInsuranceStatusRequest generatedRequest =
                    externalInsuranceMapper.toGeneratedUpdateInsuranceStatusRequest(request);
            final com.policytracker.externalinsurance.generated.model.UpdateInsuranceStatusResponse generatedResponse =
                    generatedExternalInsuranceClient.updateInsuranceStatus(generatedRequest);
            final InsuranceStatusUpdateResult insuranceStatusUpdateResult =
                    externalInsuranceMapper.toApiInsuranceStatusUpdateResult(generatedResponse);
            eventPublisher.publishEvent(InsuranceStatusUpdatedEvent.builder()
                    .userId(userId)
                    .policyId(insuranceStatusUpdateResult.getPolicyId())
                    .status(insuranceStatusUpdateResult.getStatus().name())
                    .updatedAt(insuranceStatusUpdateResult.getUpdatedAt())
                    .build());
            return insuranceStatusUpdateResult;
        } catch (RestClientException ex) {
            throw new ExternalInsuranceCommunicationException("Failed to update insurance status in external provider", ex);
        }
    }
}
