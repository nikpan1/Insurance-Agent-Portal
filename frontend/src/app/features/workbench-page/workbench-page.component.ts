import { Component, ViewChild, inject } from '@angular/core';
import { FormControl, ReactiveFormsModule, Validators } from '@angular/forms';

import { ContextClientFlowComponent } from '../context-client-flow/context-client-flow.component';
import { ExternalInsuranceFlowComponent } from '../external-insurance-flow/external-insurance-flow.component';
import { SystemReferenceFlowComponent } from '../system-reference-flow/system-reference-flow.component';
import { RequestContextService } from '../../core/request-context/request-context.service';

@Component({
  selector: 'app-workbench-page',
  imports: [ReactiveFormsModule, ContextClientFlowComponent, ExternalInsuranceFlowComponent, SystemReferenceFlowComponent],
  templateUrl: './workbench-page.component.html',
  styleUrl: './workbench-page.component.scss'
})
export class WorkbenchPageComponent {
  @ViewChild(ContextClientFlowComponent)
  private contextClientFlow?: ContextClientFlowComponent;

  private readonly requestContext = inject(RequestContextService);

  protected readonly currentUserIdControl = new FormControl('101', {
    nonNullable: true,
    validators: [Validators.required, Validators.pattern(/^[1-9]\d*$/)]
  });

  constructor() {
    this.currentUserIdControl.valueChanges.subscribe((value) => {
      if (value && this.currentUserIdControl.valid) {
        this.requestContext.setCurrentUserId(Number(value));
      }
    });
  }

  protected get currentUserId(): number {
    const value = this.currentUserIdControl.value;
    if (value && this.currentUserIdControl.valid) {
      return Number(value);
    }

    return this.requestContext.currentUserId();
  }

  protected refreshAuditForCurrentUser(): void {
    this.contextClientFlow?.refreshAuditEventsForCurrentUser();
  }
}
