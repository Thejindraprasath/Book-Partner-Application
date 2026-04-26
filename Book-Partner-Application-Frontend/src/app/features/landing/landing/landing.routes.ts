import { Routes } from '@angular/router';

export const landingRoutes: Routes = [
  // This is the first page shown for "/".
  {
    path: '',
    loadComponent: () =>
      import('./landing').then((m) => m.Landing),
  },
];
