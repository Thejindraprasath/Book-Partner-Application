import { Routes } from '@angular/router';

export const authRoutes: Routes = [
  // This route shows the login screen for the selected module.
  {
    path: '',
    loadComponent: () =>
      import('./login/login').then((m) => m.Login),
  },
];
