package com.policytracker.client.rest;

import com.policytracker.client.dto.ExternalInsurancePolicyDto;
import com.policytracker.client.dto.ExternalInsuranceUserDataResponseDto;
import com.policytracker.client.dto.InsuranceStatusUpdateResultDto;
import com.policytracker.client.dto.UpdateInsuranceStatusRequestDto;
import com.policytracker.externalinsurance.api.ExternalInsuranceService;
import com.policytracker.externalinsurance.api.dto.InsurancePolicy;
import com.policytracker.externalinsurance.api.dto.InsuranceStatusUpdateResult;
import com.policytracker.externalinsurance.api.dto.InsuranceUserData;
import com.policytracker.externalinsurance.api.dto.InsuranceUserDataRequest;
import com.policytracker.externalinsurance.api.dto.UpdateInsuranceStatusRequest;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/external-insurance")
@RequiredArgsConstructor
public class ExternalInsuranceController {

    private final ExternalInsuranceService externalInsuranceService;

    @GetMapping("/users/{externalUserId}")
    public ExternalInsuranceUserDataResponseDto getInsuranceUserData(
            @PathVariable String externalUserId,
            @RequestParam String correlationId
    ) {
        InsuranceUserData insuranceUserData = externalInsuranceService.getInsuranceUserData(
                InsuranceUserDataRequest.builder()
                        .externalUserId(externalUserId)
                        .correlationId(correlationId)
                        .build()
        );

        List<ExternalInsurancePolicyDto> policies = insuranceUserData.getActivePolicies() == null
                ? List.of()
                : insuranceUserData.getActivePolicies().stream().map(this::toPolicyDto).toList();

        return new ExternalInsuranceUserDataResponseDto(
                insuranceUserData.getExternalUserId(),
                insuranceUserData.getFirstName(),
                insuranceUserData.getLastName(),
                policies,
                insuranceUserData.getRiskScore()
        );
    }

    @PutMapping("/policies/{policyId}/status")
    public InsuranceStatusUpdateResultDto updateInsuranceStatus(
            @PathVariable String policyId,
            @Valid @RequestBody UpdateInsuranceStatusRequestDto request
    ) {
        InsuranceStatusUpdateResult result = externalInsuranceService.updateInsuranceStatus(
                UpdateInsuranceStatusRequest.builder()
                        .policyId(policyId)
                        .status(request.status())
                        .reason(request.reason())
                        .effectiveDate(request.effectiveDate())
                        .build()
        );

        return new InsuranceStatusUpdateResultDto(result.getPolicyId(), result.getStatus(), result.getUpdatedAt());
    }

    private ExternalInsurancePolicyDto toPolicyDto(InsurancePolicy policy) {
        return new ExternalInsurancePolicyDto(policy.getPolicyId(), policy.getType(), policy.getStatus());
    }
}
