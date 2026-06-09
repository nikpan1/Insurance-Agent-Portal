import { Component, input } from '@angular/core';

import { ProblemDetail } from '../../core/api/api.types';

@Component({
  selector: 'app-problem-detail-alert',
  templateUrl: './problem-detail-alert.component.html',
  styleUrl: './problem-detail-alert.component.scss'
})
export class ProblemDetailAlertComponent {
  readonly problem = input<ProblemDetail | null>(null);
}
