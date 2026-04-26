import { Routes } from '@angular/router';

import { STORES_ENDPOINTS, STORES_MODULE_ID, STORES_ROUTE } from './stores.data';

const storeEndpointRunner = () =>
  import('../../shared/components/endpoint-runner/endpoint-runner').then(
    (m) => m.EndpointRunner,
  );

// Build one child route per store action so the shared endpoint runner can load it by URL.
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
  // Main page for the store module.
  {
    path: '',
    loadComponent: () =>
      import('./pages/stores-page').then((m) => m.StoresPage),
  },
  // Action pages like add, update, delete, and search are added here automatically.
  ...endpointRoutes,
];
