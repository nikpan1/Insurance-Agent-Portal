import { Routes } from '@angular/router';

import { ClientRegistrationComponent } from './client-registration/client-registration.component';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'register'
  },
  {
    path: 'register',
    component: ClientRegistrationComponent
  },
  {
    path: '**',
    redirectTo: 'register'
  }
];
