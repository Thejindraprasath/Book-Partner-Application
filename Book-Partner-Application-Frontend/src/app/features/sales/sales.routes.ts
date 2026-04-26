import { Routes } from '@angular/router';

import { SALES_ENDPOINTS, SALES_MODULE_ID, SALES_ROUTE } from './sales.data';

const salesEndpointRunner = () =>
  import('../../shared/components/endpoint-runner/endpoint-runner').then((m) => m.EndpointRunner);

// Build one child route per sales action so the shared endpoint runner can load it by URL.
const endpointRoutes: Routes = SALES_ENDPOINTS.map((endpoint) => {
  return {
    path: endpoint.route,
    loadComponent: salesEndpointRunner,
    data: {
      moduleId: SALES_MODULE_ID,
      moduleRoute: SALES_ROUTE,
      endpoint,
    },
  };
});

export const salesRoutes: Routes = [
  // Main page for the sales module.
  {
    path: '',
    loadComponent: () =>
      import('./pages/sales-page').then((m) => m.SalesPage),
  },
  // Action pages like add, update, delete, and search are added here automatically.
  ...endpointRoutes,
];
