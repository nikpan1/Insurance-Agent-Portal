export type InsuranceStatus = 'ACTIVE' | 'SUSPENDED' | 'CANCELLED' | 'EXPIRED';

export interface ProblemDetail {
  type?: string;
  title?: string;
  status?: number;
  detail?: string;
  instance?: string;
}

export interface ClientRegistrationRequest {
  firstName: string;
  lastName: string;
  email: string;
  pesel: string;
}

export interface CreateAuditEventRequest {
  userId: number;
  eventType: string;
  metadata?: Record<string, string>;
}

export interface AuditEventResponse {
  id: string;
  userId: number;
  eventType: string;
  timestamp: string;
  metadata?: Record<string, string>;
}

export interface ExternalInsurancePolicy {
  policyId: string;
  type: string;
  status: InsuranceStatus;
}

export interface ExternalInsuranceUserDataResponse {
  externalUserId: string;
  firstName: string;
  lastName: string;
  activePolicies: ExternalInsurancePolicy[];
  riskScore: number;
}

export interface UpdateInsuranceStatusRequest {
  status: InsuranceStatus;
  reason: string;
  effectiveDate: string;
}

export interface InsuranceStatusUpdateResult {
  policyId: string;
  status: InsuranceStatus;
  updatedAt: string;
}

export interface SystemPropertyUpsertRequest {
  propertyValue: string;
}

export interface SystemPropertyResponse {
  customerId: number;
  propertyKey: string;
  propertyValue: string;
  version: number;
}

export interface ReferenceDataImportRequest {
  sourcePath: string;
}

export interface ReferenceDataNode {
  id: string;
  parentId: string | null;
  name: string;
  children: ReferenceDataNode[];
}

export interface ReferenceDataImportResult {
  source: string;
  importedRecords: number;
  importedAt: string;
  roots: ReferenceDataNode[];
}
