import { Routes } from '@angular/router';

import { AUTHORS_ENDPOINTS, AUTHORS_MODULE, AUTHORS_ROUTE } from './authors.data';

const authorEndpointRunner = () =>
  import('../../shared/components/endpoint-runner/endpoint-runner').then(
    (m) => m.EndpointRunner
  );

const endpointRoutes: Routes = AUTHORS_ENDPOINTS.map((endpoint) => ({
  path: endpoint.route,
  loadComponent: authorEndpointRunner,
  data: {
    moduleId: AUTHORS_MODULE.id,
    moduleRoute: AUTHORS_ROUTE,
    endpoint,
  },
}));

export const authorsRoutes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./pages/authors-page').then((m) => m.AuthorsPage),
  },
  ...endpointRoutes,
];
