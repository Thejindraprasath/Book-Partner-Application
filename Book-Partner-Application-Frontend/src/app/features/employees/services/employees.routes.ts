import { Routes } from '@angular/router';

import { getEndpointsForModule } from '../../config/api.config';

const endpointRoutes: Routes = getEndpointsForModule('employee').map((endpoint) => ({
  path: endpoint.route,
  loadComponent: () =>
    import('../../shared/components/endpoint-runner/endpoint-runner').then((m) => m.EndpointRunner),
  data: { moduleId: 'employee', moduleRoute: '/sanjai/employees', endpoint },
}));

export const employeesRoutes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./pages/employees-page/employees-page').then((m) => m.EmployeesPage),
  },
  ...endpointRoutes,
];
