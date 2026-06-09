import { Component, input, inject } from '@angular/core';
import { JsonPipe } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { finalize } from 'rxjs';

import { WorkbenchApiService } from '../../core/api/workbench-api.service';
import { AuditEventResponse, ProblemDetail } from '../../core/api/api.types';
import { toProblemDetail } from '../../core/api/http-error.util';
import { ApiResultPanelComponent } from '../../shared/api-result-panel/api-result-panel.component';

type ClientRegistrationForm = FormGroup<{
  firstName: FormControl<string>;
  lastName: FormControl<string>;
  email: FormControl<string>;
  pesel: FormControl<string>;
}>;

type AuditEventForm = FormGroup<{
  eventType: FormControl<string>;
  metadataJson: FormControl<string>;
}>;

@Component({
  selector: 'app-context-client-flow',
  imports: [ReactiveFormsModule, JsonPipe, ApiResultPanelComponent],
  templateUrl: './context-client-flow.component.html',
  styleUrl: './context-client-flow.component.scss'
})
export class ContextClientFlowComponent {
  readonly userId = input.required<number>();

  private readonly api = inject(WorkbenchApiService);

  protected readonly clientForm: ClientRegistrationForm = new FormGroup({
    firstName: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    lastName: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    email: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.email] }),
    pesel: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.pattern(/^\d{11}$/)]
    })
  });

  protected readonly auditEventForm: AuditEventForm = new FormGroup({
    eventType: new FormControl('MANUAL_DEMO_EVENT', { nonNullable: true, validators: [Validators.required] }),
    metadataJson: new FormControl('{"source":"workbench"}', { nonNullable: true })
  });

  protected registrationResult: unknown | null = null;
  protected registrationSuccess: string | null = null;
  protected registrationProblem: ProblemDetail | null = null;
  protected isSubmittingClient = false;

  protected auditEvents: AuditEventResponse[] = [];
  protected auditProblem: ProblemDetail | null = null;
  protected auditSuccess: string | null = null;
  protected isLoadingAudit = false;

  protected manualAuditProblem: ProblemDetail | null = null;
  protected manualAuditSuccess: string | null = null;
  protected isCreatingAudit = false;

  refreshAuditEventsForCurrentUser(): void {
    this.loadAuditEvents();
  }

  protected registerClient(): void {
    if (this.clientForm.invalid) {
      this.clientForm.markAllAsTouched();
      return;
    }

    this.isSubmittingClient = true;
    this.registrationSuccess = null;
    this.registrationProblem = null;

    this.api
      .registerClient(this.clientForm.getRawValue())
      .pipe(finalize(() => (this.isSubmittingClient = false)))
      .subscribe({
        next: () => {
          this.registrationSuccess = 'Client registered successfully.';
          this.registrationResult = this.clientForm.getRawValue();
        },
        error: (error: unknown) => {
          this.registrationProblem = toProblemDetail(error);
          this.registrationResult = null;
        }
      });
  }

  protected loadAuditEvents(): void {
    this.isLoadingAudit = true;
    this.auditSuccess = null;
    this.auditProblem = null;

    this.api
      .getAuditEvents(this.userId())
      .pipe(finalize(() => (this.isLoadingAudit = false)))
      .subscribe({
        next: (events) => {
          this.auditEvents = events;
          this.auditSuccess = `Loaded ${events.length} audit event(s).`;
        },
        error: (error: unknown) => {
          this.auditEvents = [];
          this.auditProblem = toProblemDetail(error);
        }
      });
  }

  protected createManualAuditEvent(): void {
    if (this.auditEventForm.invalid) {
      this.auditEventForm.markAllAsTouched();
      return;
    }

    const metadata = this.parseMetadata(this.auditEventForm.controls.metadataJson.value);
    if (metadata === null) {
      this.manualAuditProblem = {
        title: 'Invalid metadata JSON',
        detail: 'Metadata must be a JSON object with string values.'
      };
      return;
    }

    this.isCreatingAudit = true;
    this.manualAuditProblem = null;
    this.manualAuditSuccess = null;

    this.api
      .createAuditEvent({
        userId: this.userId(),
        eventType: this.auditEventForm.controls.eventType.value,
        metadata
      })
      .pipe(finalize(() => (this.isCreatingAudit = false)))
      .subscribe({
        next: () => {
          this.manualAuditSuccess = 'Manual audit event created.';
          this.loadAuditEvents();
        },
        error: (error: unknown) => {
          this.manualAuditProblem = toProblemDetail(error);
        }
      });
  }

  private parseMetadata(rawValue: string): Record<string, string> | undefined | null {
    if (!rawValue.trim()) {
      return undefined;
    }

    try {
      const parsed = JSON.parse(rawValue) as unknown;

      if (!parsed || typeof parsed !== 'object' || Array.isArray(parsed)) {
        return null;
      }

      const entries = Object.entries(parsed as Record<string, unknown>);
      if (entries.some(([, value]) => typeof value !== 'string')) {
        return null;
      }

      return Object.fromEntries(entries) as Record<string, string>;
    } catch {
      return null;
    }
  }
}
