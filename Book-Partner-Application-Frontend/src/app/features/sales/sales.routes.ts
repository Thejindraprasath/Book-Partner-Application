import { Routes } from '@angular/router';

import { SALES_ENDPOINTS, SALES_MODULE_ID, SALES_ROUTE } from './sales.data';

const salesEndpointRunner = () =>
  import('../../shared/components/endpoint-runner/endpoint-runner').then((m) => m.EndpointRunner);

// We build one route per endpoint so every sales API screen has its own URL.
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
  {
    path: '',
    loadComponent: () =>
      import('./pages/sales-page/sales-page').then((m) => m.SalesPage),
  },
  ...endpointRoutes,
];
