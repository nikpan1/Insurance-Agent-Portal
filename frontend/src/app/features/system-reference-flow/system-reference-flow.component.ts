import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { NgTemplateOutlet } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { finalize } from 'rxjs';

import {
  ProblemDetail,
  ReferenceDataImportResult,
  ReferenceDataNode,
  SystemPropertyResponse
} from '../../core/api/api.types';
import { toProblemDetail } from '../../core/api/http-error.util';
import { WorkbenchApiService } from '../../core/api/workbench-api.service';
import { ApiResultPanelComponent } from '../../shared/api-result-panel/api-result-panel.component';

type PropertyForm = FormGroup<{
  customerId: FormControl<string>;
  propertyKey: FormControl<string>;
  propertyValue: FormControl<string>;
}>;

type ImportForm = FormGroup<{
  sourcePath: FormControl<string>;
}>;

@Component({
  selector: 'app-system-reference-flow',
  imports: [ReactiveFormsModule, NgTemplateOutlet, ApiResultPanelComponent],
  templateUrl: './system-reference-flow.component.html',
  styleUrl: './system-reference-flow.component.scss'
})
export class SystemReferenceFlowComponent {
  private readonly api = inject(WorkbenchApiService);

  protected readonly propertyForm: PropertyForm = new FormGroup({
    customerId: new FormControl('101', {
      nonNullable: true,
      validators: [Validators.required, Validators.pattern(/^[1-9]\d*$/)]
    }),
    propertyKey: new FormControl('externalinsurance.events.enabled', {
      nonNullable: true,
      validators: [Validators.required]
    }),
    propertyValue: new FormControl('true', {
      nonNullable: true,
      validators: [Validators.required]
    })
  });

  protected readonly importForm: ImportForm = new FormGroup({
    sourcePath: new FormControl('./data/reference-data.csv', {
      nonNullable: true,
      validators: [Validators.required]
    })
  });

  protected propertyResult: SystemPropertyResponse | null = null;
  protected propertySuccess: string | null = null;
  protected propertyProblem: ProblemDetail | null = null;
  protected isLoadingProperty = false;
  protected isSavingProperty = false;

  protected importResult: ReferenceDataImportResult | null = null;
  protected importSuccess: string | null = null;
  protected importProblem: ProblemDetail | null = null;
  protected isImporting = false;

  protected treeNodes: ReferenceDataNode[] = [];
  protected treeProblem: ProblemDetail | null = null;
  protected treeSuccess: string | null = null;
  protected isLoadingTree = false;

  protected upsertProperty(): void {
    if (this.propertyForm.invalid) {
      this.propertyForm.markAllAsTouched();
      return;
    }

    const customerId = Number(this.propertyForm.controls.customerId.value);
    const propertyKey = this.propertyForm.controls.propertyKey.value;
    const propertyValue = this.propertyForm.controls.propertyValue.value;

    this.isSavingProperty = true;
    this.propertyProblem = null;
    this.propertySuccess = null;

    this.api
      .upsertSystemProperty(customerId, propertyKey, { propertyValue })
      .pipe(finalize(() => (this.isSavingProperty = false)))
      .subscribe({
        next: (property) => {
          this.propertyResult = property;
          this.propertySuccess = 'Property saved.';
        },
        error: (error: unknown) => {
          this.propertyResult = null;
          this.propertyProblem = toProblemDetail(error);
        }
      });
  }

  protected loadProperty(): void {
    if (this.propertyForm.controls.customerId.invalid || this.propertyForm.controls.propertyKey.invalid) {
      this.propertyForm.controls.customerId.markAsTouched();
      this.propertyForm.controls.propertyKey.markAsTouched();
      return;
    }

    const customerId = Number(this.propertyForm.controls.customerId.value);
    const propertyKey = this.propertyForm.controls.propertyKey.value;

    this.isLoadingProperty = true;
    this.propertyProblem = null;
    this.propertySuccess = null;

    this.api
      .getSystemProperty(customerId, propertyKey)
      .pipe(finalize(() => (this.isLoadingProperty = false)))
      .subscribe({
        next: (property) => {
          this.propertyResult = property;
          this.propertyForm.controls.propertyValue.setValue(property.propertyValue);
          this.propertySuccess = 'Property loaded.';
        },
        error: (error: unknown) => {
          this.propertyResult = null;

          if (error instanceof HttpErrorResponse && error.status === 404) {
            this.propertyProblem = {
              title: 'Property not found',
              status: 404,
              detail: 'No property exists for the selected customer and key.'
            };
            return;
          }

          this.propertyProblem = toProblemDetail(error);
        }
      });
  }

  protected importReferenceData(): void {
    if (this.importForm.invalid) {
      this.importForm.markAllAsTouched();
      return;
    }

    this.isImporting = true;
    this.importProblem = null;
    this.importSuccess = null;

    this.api
      .importReferenceData(this.importForm.getRawValue())
      .pipe(finalize(() => (this.isImporting = false)))
      .subscribe({
        next: (result) => {
          this.importResult = result;
          this.importSuccess = `Import completed with ${result.importedRecords} record(s).`;
          this.loadTree();
        },
        error: (error: unknown) => {
          this.importResult = null;
          this.importProblem = toProblemDetail(error);
        }
      });
  }

  protected loadTree(): void {
    this.isLoadingTree = true;
    this.treeProblem = null;
    this.treeSuccess = null;

    this.api
      .getReferenceDataTree()
      .pipe(finalize(() => (this.isLoadingTree = false)))
      .subscribe({
        next: (nodes) => {
          this.treeNodes = nodes;
          this.treeSuccess = `Loaded ${nodes.length} root node(s).`;
        },
        error: (error: unknown) => {
          this.treeNodes = [];
          this.treeProblem = toProblemDetail(error);
        }
      });
  }
}
