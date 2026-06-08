package com.policytracker.externalinsurance.api;

import com.policytracker.common.architecture.DomainApi;

public interface ExternalInsuranceService extends DomainApi {

    InsuranceUserData getInsuranceUserData(InsuranceUserDataRequest request);

    InsuranceStatusUpdateResult updateInsuranceStatus(UpdateInsuranceStatusRequest request);
}
