import { Routes } from '@angular/router';

import { EMPLOYEES_ENDPOINTS, EMPLOYEES_MODULE_ID, EMPLOYEES_ROUTE } from './employees.data';

const employeeEndpointRunner = () =>
  import('../../shared/components/endpoint-runner/endpoint-runner').then(
    (m) => m.EndpointRunner,
  );

// Build one child route per employee action so the shared endpoint runner can load it by URL.
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
  // Main page for the employee module.
  {
    path: '',
    loadComponent: () =>
      import('./pages/employees-page').then((m) => m.EmployeesPage),
  },
  // Action pages like add, update, delete, and search are added here automatically.
  ...endpointRoutes,
];
