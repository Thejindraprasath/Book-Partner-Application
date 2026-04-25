import { Routes } from '@angular/router';

import { STORES_ENDPOINTS, STORES_MODULE_ID, STORES_ROUTE } from './stores.data';

const storeEndpointRunner = () =>
  import('../../shared/components/endpoint-runner/endpoint-runner').then(
    (m) => m.EndpointRunner,
  );

// Each endpoint gets its own route so it can open inside the shared runner.
const endpointRoutes: Routes = STORES_ENDPOINTS.map((endpoint) => {
  return {
    path: endpoint.route,
    loadComponent: storeEndpointRunner,
    data: {
      moduleId: STORES_MODULE_ID,
      moduleRoute: STORES_ROUTE,
      endpoint,
    },
  };
});

export const storesRoutes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./pages/stores-page').then((m) => m.StoresPage),
  },
  ...endpointRoutes,
];
