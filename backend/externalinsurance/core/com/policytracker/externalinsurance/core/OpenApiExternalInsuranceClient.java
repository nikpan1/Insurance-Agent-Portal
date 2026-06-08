package com.policytracker.externalinsurance.core;

import com.policytracker.externalinsurance.generated.api.DefaultApi;
import com.policytracker.externalinsurance.generated.model.InsuranceUserDataRequest;
import com.policytracker.externalinsurance.generated.model.InsuranceUserDataResponse;
import com.policytracker.externalinsurance.generated.model.UpdateInsuranceStatusRequest;
import com.policytracker.externalinsurance.generated.model.UpdateInsuranceStatusResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OpenApiExternalInsuranceClient implements GeneratedExternalInsuranceClient {

    private final DefaultApi defaultApi;

    @Override
    public InsuranceUserDataResponse getInsuranceUserData(final InsuranceUserDataRequest request) {
        return defaultApi.getInsuranceUserData(request);
    }

    @Override
    public UpdateInsuranceStatusResponse updateInsuranceStatus(final UpdateInsuranceStatusRequest request) {
        return defaultApi.updateInsuranceStatus(request);
    }
}
