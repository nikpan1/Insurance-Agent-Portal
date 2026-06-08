package com.policytracker.externalinsurance.api;

import com.policytracker.common.architecture.DomainApi;
import com.policytracker.externalinsurance.api.dto.InsuranceStatusUpdateResult;
import com.policytracker.externalinsurance.api.dto.InsuranceUserData;
import com.policytracker.externalinsurance.api.dto.InsuranceUserDataRequest;
import com.policytracker.externalinsurance.api.dto.UpdateInsuranceStatusRequest;

public interface ExternalInsuranceService extends DomainApi {

    InsuranceUserData getInsuranceUserData(InsuranceUserDataRequest request);

    InsuranceStatusUpdateResult updateInsuranceStatus(UpdateInsuranceStatusRequest request);
}
