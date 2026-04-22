import { Routes } from '@angular/router';

import { roleGuard } from '../../core/guards/role.guard';

export const sanjaiRoutes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./pages/sanjai-page/sanjai-page').then((m) => m.SanjaiPage),
  },
  {
    path: 'publishers',
    canActivate: [roleGuard],
    data: { roles: ['ROLE_PUBLISHER'] },
    loadChildren: () =>
      import('../publishers/publishers.routes').then((m) => m.publishersRoutes),
  },
  {
    path: 'employees',
    canActivate: [roleGuard],
    data: { roles: ['ROLE_EMPLOYEE'] },
    loadChildren: () =>
      import('../employees/employees.routes').then((m) => m.employeesRoutes),
  },
];
