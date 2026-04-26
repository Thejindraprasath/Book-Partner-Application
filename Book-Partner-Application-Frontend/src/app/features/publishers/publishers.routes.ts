import { Routes } from '@angular/router';

import {
  PUBLISHERS_ENDPOINTS,
  PUBLISHERS_MODULE_ID,
  PUBLISHERS_ROUTE,
} from './publishers.data';

const publisherEndpointRunner = () =>
  import('../../shared/components/endpoint-runner/endpoint-runner').then(
    (m) => m.EndpointRunner,
  );

// Build one child route per publisher action so the shared endpoint runner can load it by URL.
const endpointRoutes: Routes = PUBLISHERS_ENDPOINTS.map((endpoint) => {
  return {
    path: endpoint.route,
    loadComponent: publisherEndpointRunner,
    data: {
      moduleId: PUBLISHERS_MODULE_ID,
      moduleRoute: PUBLISHERS_ROUTE,
      endpoint,
    },
  };
});

export const publishersRoutes: Routes = [
  // Main page for the publisher module.
  {
    path: '',
    loadComponent: () =>
      import('./pages/publishers-page').then((m) => m.PublishersPage),
  },
  // Action pages like add, update, delete, and search are added here automatically.
  ...endpointRoutes,
];
