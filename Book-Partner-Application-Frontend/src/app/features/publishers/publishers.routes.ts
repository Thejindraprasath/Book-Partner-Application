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

// Each endpoint gets its own route so it can open inside the shared runner.
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
  {
    path: '',
    loadComponent: () =>
      import('./pages/publishers-page').then((m) => m.PublishersPage),
  },
  ...endpointRoutes,
];
