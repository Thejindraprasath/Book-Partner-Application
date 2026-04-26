import { Routes } from '@angular/router';

import { roleGuard } from '../../core/guards/role.guard';

export const sanjaiRoutes: Routes = [
  // Main page for the Sanjai module.
  {
    path: '',
    loadComponent: () =>
      import('./pages/sanjai-page').then((m) => m.SanjaiPage),
  },
  // Publisher pages live under /sanjai/publishers and require publisher role access.
  {
    path: 'publishers',
    canActivate: [roleGuard],
    data: { roles: ['ROLE_PUBLISHER'] },
    loadChildren: () =>
      import('../publishers/publishers.routes').then((m) => m.publishersRoutes),
  },
  // Employee pages live under /sanjai/employees and require employee role access.
  {
    path: 'employees',
    canActivate: [roleGuard],
    data: { roles: ['ROLE_EMPLOYEE'] },
    loadChildren: () =>
      import('../employees/employees.routes').then((m) => m.employeesRoutes),
  },
];
