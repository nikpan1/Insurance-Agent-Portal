package com.policytracker.externalinsurance.core;

import com.policytracker.externalinsurance.api.InsurancePolicy;
import com.policytracker.externalinsurance.api.InsuranceStatus;
import com.policytracker.externalinsurance.api.InsuranceStatusUpdateResult;
import com.policytracker.externalinsurance.api.InsuranceUserData;
import com.policytracker.externalinsurance.api.InsuranceUserDataRequest;
import com.policytracker.externalinsurance.api.UpdateInsuranceStatusRequest;
import com.policytracker.externalinsurance.generated.model.InsuranceUserDataResponseActivePoliciesInner;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ExternalInsuranceMapper {

    com.policytracker.externalinsurance.generated.model.InsuranceUserDataRequest toGeneratedInsuranceUserDataRequest(InsuranceUserDataRequest request);

    InsuranceUserData toApiInsuranceUserData(com.policytracker.externalinsurance.generated.model.InsuranceUserDataResponse response);

    @Mapping(target = "status", expression = "java(toGeneratedStatus(request.getStatus()))")
    com.policytracker.externalinsurance.generated.model.UpdateInsuranceStatusRequest toGeneratedUpdateInsuranceStatusRequest(UpdateInsuranceStatusRequest request);

    @Mapping(target = "status", expression = "java(toApiStatus(response.getStatus()))")
    InsuranceStatusUpdateResult toApiInsuranceStatusUpdateResult(com.policytracker.externalinsurance.generated.model.UpdateInsuranceStatusResponse response);

    @Mapping(target = "status", expression = "java(toApiStatus(policy.getStatus()))")
    InsurancePolicy toApiPolicy(InsuranceUserDataResponseActivePoliciesInner policy);

    default com.policytracker.externalinsurance.generated.model.UpdateInsuranceStatusRequest.StatusEnum toGeneratedStatus(final InsuranceStatus status) {
        if (status == null) {
            return null;
        }
        return com.policytracker.externalinsurance.generated.model.UpdateInsuranceStatusRequest.StatusEnum.fromValue(status.name());
    }

    default InsuranceStatus toApiStatus(final String status) {
        if (status == null) {
            return null;
        }
        return InsuranceStatus.valueOf(status);
    }
}
