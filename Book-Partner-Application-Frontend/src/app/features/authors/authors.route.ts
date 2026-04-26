import { Routes } from '@angular/router';

import { AUTHORS_ENDPOINTS, AUTHORS_MODULE, AUTHORS_ROUTE } from './authors.data';

const authorEndpointRunner = () =>
  import('../../shared/components/endpoint-runner/endpoint-runner').then(
    (m) => m.EndpointRunner
  );

// Build one child route per author action so the shared endpoint runner can load it by URL.
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
  // Main page for the author module.
  {
    path: '',
    loadComponent: () =>
      import('./pages/authors-page').then((m) => m.AuthorsPage),
  },
  // Action pages like add, update, delete, and search are added here automatically.
  ...endpointRoutes,
];
