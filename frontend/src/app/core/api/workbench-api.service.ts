import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import {
  AuditEventResponse,
  ClientRegistrationRequest,
  CreateAuditEventRequest,
  ExternalInsuranceUserDataResponse,
  InsuranceStatusUpdateResult,
  ReferenceDataImportRequest,
  ReferenceDataImportResult,
  ReferenceDataNode,
  SystemPropertyResponse,
  SystemPropertyUpsertRequest,
  UpdateInsuranceStatusRequest
} from './api.types';

@Injectable({
  providedIn: 'root'
})
export class WorkbenchApiService {
  private readonly http = inject(HttpClient);
  private readonly apiBaseUrl = '/api/v1';

  registerClient(payload: ClientRegistrationRequest): Observable<void> {
    return this.http.post<void>(`${this.apiBaseUrl}/clients`, payload);
  }

  getAuditEvents(userId: number): Observable<AuditEventResponse[]> {
    return this.http.get<AuditEventResponse[]>(`${this.apiBaseUrl}/audit-events/users/${userId}`);
  }

  createAuditEvent(payload: CreateAuditEventRequest): Observable<AuditEventResponse> {
    return this.http.post<AuditEventResponse>(`${this.apiBaseUrl}/audit-events`, payload);
  }

  getExternalInsuranceUserData(externalUserId: string, correlationId: string): Observable<ExternalInsuranceUserDataResponse> {
    const params = new HttpParams().set('correlationId', correlationId);
    return this.http.get<ExternalInsuranceUserDataResponse>(
      `${this.apiBaseUrl}/external-insurance/users/${externalUserId}`,
      { params }
    );
  }

  updateInsuranceStatus(policyId: string, payload: UpdateInsuranceStatusRequest): Observable<InsuranceStatusUpdateResult> {
    return this.http.put<InsuranceStatusUpdateResult>(
      `${this.apiBaseUrl}/external-insurance/policies/${policyId}/status`,
      payload
    );
  }

  upsertSystemProperty(
    customerId: number,
    propertyKey: string,
    payload: SystemPropertyUpsertRequest
  ): Observable<SystemPropertyResponse> {
    return this.http.put<SystemPropertyResponse>(
      `${this.apiBaseUrl}/system-properties/${customerId}/${propertyKey}`,
      payload
    );
  }

  getSystemProperty(customerId: number, propertyKey: string): Observable<SystemPropertyResponse> {
    return this.http.get<SystemPropertyResponse>(`${this.apiBaseUrl}/system-properties/${customerId}/${propertyKey}`);
  }

  importReferenceData(payload: ReferenceDataImportRequest): Observable<ReferenceDataImportResult> {
    return this.http.post<ReferenceDataImportResult>(`${this.apiBaseUrl}/reference-data/import`, payload);
  }

  getReferenceDataTree(): Observable<ReferenceDataNode[]> {
    return this.http.get<ReferenceDataNode[]>(`${this.apiBaseUrl}/reference-data/tree`);
  }
}
