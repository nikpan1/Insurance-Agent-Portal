import { Component, input } from '@angular/core';
import { JsonPipe } from '@angular/common';

import { ProblemDetail } from '../../core/api/api.types';
import { ProblemDetailAlertComponent } from '../problem-detail-alert/problem-detail-alert.component';

@Component({
  selector: 'app-api-result-panel',
  imports: [JsonPipe, ProblemDetailAlertComponent],
  templateUrl: './api-result-panel.component.html',
  styleUrl: './api-result-panel.component.scss'
})
export class ApiResultPanelComponent {
  readonly title = input('Result');
  readonly successMessage = input<string | null>(null);
  readonly payload = input<unknown | null>(null);
  readonly problem = input<ProblemDetail | null>(null);

  protected hasContent(): boolean {
    return this.successMessage() !== null || this.payload() !== null || this.problem() !== null;
  }
}
