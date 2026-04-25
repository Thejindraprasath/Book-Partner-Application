import { Routes } from '@angular/router';

import { EMPLOYEES_ENDPOINTS, EMPLOYEES_MODULE_ID, EMPLOYEES_ROUTE } from './employees.data';

const employeeEndpointRunner = () =>
  import('../../shared/components/endpoint-runner/endpoint-runner').then(
    (m) => m.EndpointRunner,
  );

// Each endpoint gets its own route so it can open inside the shared runner.
const endpointRoutes: Routes = EMPLOYEES_ENDPOINTS.map((endpoint) => {
  return {
    path: endpoint.route,
    loadComponent: employeeEndpointRunner,
    data: {
      moduleId: EMPLOYEES_MODULE_ID,
      moduleRoute: EMPLOYEES_ROUTE,
      endpoint,
    },
  };
});

export const employeesRoutes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./pages/employees-page').then((m) => m.EmployeesPage),
  },
  ...endpointRoutes,
];
