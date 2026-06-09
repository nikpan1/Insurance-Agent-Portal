import { Routes } from '@angular/router';

import { WorkbenchPageComponent } from './features/workbench-page/workbench-page.component';

export const routes: Routes = [
  {
    path: '',
    component: WorkbenchPageComponent
  },
  {
    path: '**',
    redirectTo: ''
  }
];
