package com.policytracker.externalinsurance.generated.api;

import com.policytracker.externalinsurance.generated.invoker.ApiClient;
import com.policytracker.externalinsurance.generated.model.InsuranceUserDataRequest;
import com.policytracker.externalinsurance.generated.model.InsuranceUserDataResponse;
import com.policytracker.externalinsurance.generated.model.UpdateInsuranceStatusRequest;
import com.policytracker.externalinsurance.generated.model.UpdateInsuranceStatusResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import java.util.Collections;
import java.util.List;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2026-06-09T00:11:21.912+02:00[Europe/Belgrade]")
public class DefaultApi {
    private ApiClient apiClient;

    public DefaultApi() {
        this(new ApiClient());
    }

    public DefaultApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Get insurance user data
     * Retrieves user insurance-related data from external provider.
     * <p><b>200</b> - User insurance data retrieved successfully
     * <p><b>400</b> - Invalid request
     * <p><b>500</b> - External provider error
     *
     * @param insuranceUserDataRequest (required)
     * @return InsuranceUserDataResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public InsuranceUserDataResponse getInsuranceUserData(InsuranceUserDataRequest insuranceUserDataRequest) throws RestClientException {
        return getInsuranceUserDataWithHttpInfo(insuranceUserDataRequest).getBody();
    }

    /**
     * Get insurance user data
     * Retrieves user insurance-related data from external provider.
     * <p><b>200</b> - User insurance data retrieved successfully
     * <p><b>400</b> - Invalid request
     * <p><b>500</b> - External provider error
     *
     * @param insuranceUserDataRequest (required)
     * @return ResponseEntity&lt;InsuranceUserDataResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<InsuranceUserDataResponse> getInsuranceUserDataWithHttpInfo(InsuranceUserDataRequest insuranceUserDataRequest) throws RestClientException {
        Object localVarPostBody = insuranceUserDataRequest;

        // verify the required parameter 'insuranceUserDataRequest' is set
        if (insuranceUserDataRequest == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'insuranceUserDataRequest' when calling getInsuranceUserData");
        }


        final MultiValueMap<String, String> localVarQueryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders localVarHeaderParams = new HttpHeaders();
        final MultiValueMap<String, String> localVarCookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> localVarFormParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = {
                "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = {
                "application/json"
        };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[]{};

        ParameterizedTypeReference<InsuranceUserDataResponse> localReturnType = new ParameterizedTypeReference<InsuranceUserDataResponse>() {
        };
        return apiClient.invokeAPI("/insurance/user-data", HttpMethod.POST, Collections.<String, Object>emptyMap(), localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }

    /**
     * Update insurance status
     * Updates insurance policy status in external system.
     * <p><b>200</b> - Status updated successfully
     * <p><b>400</b> - Invalid request
     * <p><b>500</b> - External provider error
     *
     * @param updateInsuranceStatusRequest (required)
     * @return UpdateInsuranceStatusResponse
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public UpdateInsuranceStatusResponse updateInsuranceStatus(UpdateInsuranceStatusRequest updateInsuranceStatusRequest) throws RestClientException {
        return updateInsuranceStatusWithHttpInfo(updateInsuranceStatusRequest).getBody();
    }

    /**
     * Update insurance status
     * Updates insurance policy status in external system.
     * <p><b>200</b> - Status updated successfully
     * <p><b>400</b> - Invalid request
     * <p><b>500</b> - External provider error
     *
     * @param updateInsuranceStatusRequest (required)
     * @return ResponseEntity&lt;UpdateInsuranceStatusResponse&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<UpdateInsuranceStatusResponse> updateInsuranceStatusWithHttpInfo(UpdateInsuranceStatusRequest updateInsuranceStatusRequest) throws RestClientException {
        Object localVarPostBody = updateInsuranceStatusRequest;

        // verify the required parameter 'updateInsuranceStatusRequest' is set
        if (updateInsuranceStatusRequest == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'updateInsuranceStatusRequest' when calling updateInsuranceStatus");
        }


        final MultiValueMap<String, String> localVarQueryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders localVarHeaderParams = new HttpHeaders();
        final MultiValueMap<String, String> localVarCookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> localVarFormParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = {
                "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = {
                "application/json"
        };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[]{};

        ParameterizedTypeReference<UpdateInsuranceStatusResponse> localReturnType = new ParameterizedTypeReference<UpdateInsuranceStatusResponse>() {
        };
        return apiClient.invokeAPI("/insurance/status", HttpMethod.PUT, Collections.<String, Object>emptyMap(), localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
}
