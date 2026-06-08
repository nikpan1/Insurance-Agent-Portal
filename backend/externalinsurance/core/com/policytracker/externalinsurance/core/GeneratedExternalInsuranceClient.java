package com.policytracker.externalinsurance.core;

import com.policytracker.externalinsurance.generated.model.InsuranceUserDataRequest;
import com.policytracker.externalinsurance.generated.model.InsuranceUserDataResponse;
import com.policytracker.externalinsurance.generated.model.UpdateInsuranceStatusRequest;
import com.policytracker.externalinsurance.generated.model.UpdateInsuranceStatusResponse;

public interface GeneratedExternalInsuranceClient {

    InsuranceUserDataResponse getInsuranceUserData(InsuranceUserDataRequest request);

    UpdateInsuranceStatusResponse updateInsuranceStatus(UpdateInsuranceStatusRequest request);
}
