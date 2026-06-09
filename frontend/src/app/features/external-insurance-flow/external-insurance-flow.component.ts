import { Component, input, output, inject } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { finalize } from 'rxjs';

import {
  ExternalInsuranceUserDataResponse,
  InsuranceStatus,
  InsuranceStatusUpdateResult,
  ProblemDetail
} from '../../core/api/api.types';
import { toProblemDetail } from '../../core/api/http-error.util';
import { WorkbenchApiService } from '../../core/api/workbench-api.service';
import { ApiResultPanelComponent } from '../../shared/api-result-panel/api-result-panel.component';

type FetchForm = FormGroup<{
  externalUserId: FormControl<string>;
  correlationId: FormControl<string>;
}>;

type StatusUpdateForm = FormGroup<{
  status: FormControl<InsuranceStatus>;
  reason: FormControl<string>;
  effectiveDate: FormControl<string>;
}>;

@Component({
  selector: 'app-external-insurance-flow',
  imports: [ReactiveFormsModule, ApiResultPanelComponent],
  templateUrl: './external-insurance-flow.component.html',
  styleUrl: './external-insurance-flow.component.scss'
})
export class ExternalInsuranceFlowComponent {
  readonly userId = input.required<number>();
  readonly auditRefreshRequested = output<void>();

  private readonly api = inject(WorkbenchApiService);

  protected readonly statuses: InsuranceStatus[] = ['ACTIVE', 'SUSPENDED', 'CANCELLED', 'EXPIRED'];

  protected readonly fetchForm: FetchForm = new FormGroup({
    externalUserId: new FormControl('demo-user-1', { nonNullable: true, validators: [Validators.required] }),
    correlationId: new FormControl('workbench-correlation-001', { nonNullable: true, validators: [Validators.required] })
  });

  protected readonly statusUpdateForm: StatusUpdateForm = new FormGroup({
    status: new FormControl<InsuranceStatus>('ACTIVE', { nonNullable: true, validators: [Validators.required] }),
    reason: new FormControl('Manual review from workbench', {
      nonNullable: true,
      validators: [Validators.required]
    }),
    effectiveDate: new FormControl('', { nonNullable: true, validators: [Validators.required] })
  });

  protected readonly selectedPolicyIdControl = new FormControl('', { nonNullable: true, validators: [Validators.required] });
  protected userData: ExternalInsuranceUserDataResponse | null = null;
  protected fetchProblem: ProblemDetail | null = null;
  protected fetchSuccess: string | null = null;
  protected isFetching = false;

  protected statusResult: InsuranceStatusUpdateResult | null = null;
  protected statusProblem: ProblemDetail | null = null;
  protected statusSuccess: string | null = null;
  protected isUpdatingStatus = false;

  protected fetchUserData(): void {
    if (this.fetchForm.invalid) {
      this.fetchForm.markAllAsTouched();
      return;
    }

    const { externalUserId, correlationId } = this.fetchForm.getRawValue();
    this.isFetching = true;
    this.fetchProblem = null;
    this.fetchSuccess = null;

    this.api
      .getExternalInsuranceUserData(externalUserId, correlationId)
      .pipe(finalize(() => (this.isFetching = false)))
      .subscribe({
        next: (response) => {
          this.userData = response;
          this.selectedPolicyIdControl.setValue(response.activePolicies[0]?.policyId ?? '');
          this.fetchSuccess = `Loaded ${response.activePolicies.length} policy item(s).`;
        },
        error: (error: unknown) => {
          this.userData = null;
          this.selectedPolicyIdControl.setValue('');
          this.fetchProblem = toProblemDetail(error);
        }
      });
  }

  protected updatePolicyStatus(): void {
    if (this.selectedPolicyIdControl.invalid || this.statusUpdateForm.invalid) {
      this.selectedPolicyIdControl.markAsTouched();
      this.statusUpdateForm.markAllAsTouched();
      return;
    }

    this.statusProblem = null;
    this.statusSuccess = null;
    this.isUpdatingStatus = true;

    this.api
      .updateInsuranceStatus(this.selectedPolicyIdControl.value, this.statusUpdateForm.getRawValue())
      .pipe(finalize(() => (this.isUpdatingStatus = false)))
      .subscribe({
        next: (result) => {
          this.statusResult = result;
          this.statusSuccess = `Policy ${result.policyId} moved to ${result.status}.`;
          this.auditRefreshRequested.emit();
        },
        error: (error: unknown) => {
          this.statusResult = null;
          this.statusProblem = toProblemDetail(error);
        }
      });
  }
}
