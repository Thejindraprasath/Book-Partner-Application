import { Routes } from '@angular/router';

import { AUTHORS_ENDPOINTS, AUTHORS_MODULE_ID, AUTHORS_ROUTE } from './authors.data';

const authorEndpointRunner = () =>
  import('../../shared/components/endpoint-runner/endpoint-runner').then(
    (m) => m.EndpointRunner,
  );

// Each endpoint gets its own route so it can open inside the shared runner.
const endpointRoutes: Routes = AUTHORS_ENDPOINTS.map((endpoint) => {
  return {
    path: endpoint.route,
    loadComponent: authorEndpointRunner,
    data: {
      moduleId: AUTHORS_MODULE_ID,
      moduleRoute: AUTHORS_ROUTE,
      endpoint,
    },
  };
});

export const authorsRoutes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./pages/authors-page/authors-page').then((m) => m.AuthorsPage),
  },
  ...endpointRoutes,
];
