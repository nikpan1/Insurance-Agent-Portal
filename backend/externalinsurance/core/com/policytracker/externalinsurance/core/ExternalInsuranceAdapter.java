package com.policytracker.externalinsurance.core;

import com.policytracker.common.architecture.DomainCore;
import com.policytracker.externalinsurance.api.ExternalInsuranceCommunicationException;
import com.policytracker.externalinsurance.api.ExternalInsuranceService;
import com.policytracker.externalinsurance.api.InsuranceStatusUpdateResult;
import com.policytracker.externalinsurance.api.InsuranceUserData;
import com.policytracker.externalinsurance.api.InsuranceUserDataRequest;
import com.policytracker.externalinsurance.api.UpdateInsuranceStatusRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

@Service
@RequiredArgsConstructor
public class ExternalInsuranceAdapter implements ExternalInsuranceService, DomainCore {

    private final GeneratedExternalInsuranceClient generatedExternalInsuranceClient;
    private final ExternalInsuranceMapper externalInsuranceMapper;

    @Override
    public InsuranceUserData getInsuranceUserData(final InsuranceUserDataRequest request) {
        try {
            final com.policytracker.externalinsurance.generated.model.InsuranceUserDataRequest generatedRequest =
                    externalInsuranceMapper.toGeneratedInsuranceUserDataRequest(request);
            final com.policytracker.externalinsurance.generated.model.InsuranceUserDataResponse generatedResponse =
                    generatedExternalInsuranceClient.getInsuranceUserData(generatedRequest);
            return externalInsuranceMapper.toApiInsuranceUserData(generatedResponse);
        } catch (RestClientException ex) {
            throw new ExternalInsuranceCommunicationException("Failed to fetch insurance user data from external provider", ex);
        }
    }

    @Override
    public InsuranceStatusUpdateResult updateInsuranceStatus(final UpdateInsuranceStatusRequest request) {
        try {
            final com.policytracker.externalinsurance.generated.model.UpdateInsuranceStatusRequest generatedRequest =
                    externalInsuranceMapper.toGeneratedUpdateInsuranceStatusRequest(request);
            final com.policytracker.externalinsurance.generated.model.UpdateInsuranceStatusResponse generatedResponse =
                    generatedExternalInsuranceClient.updateInsuranceStatus(generatedRequest);
            return externalInsuranceMapper.toApiInsuranceStatusUpdateResult(generatedResponse);
        } catch (RestClientException ex) {
            throw new ExternalInsuranceCommunicationException("Failed to update insurance status in external provider", ex);
        }
    }
}
