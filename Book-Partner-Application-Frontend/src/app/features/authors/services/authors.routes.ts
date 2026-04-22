import { Routes } from '@angular/router';

import { getEndpointsForModule } from '../../config/api.config';

const endpointRoutes: Routes = getEndpointsForModule('author').map((endpoint) => ({
  path: endpoint.route,
  loadComponent: () =>
    import('../../shared/components/endpoint-runner/endpoint-runner').then((m) => m.EndpointRunner),
  data: { moduleId: 'author', moduleRoute: '/author', endpoint },
}));

export const authorsRoutes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./pages/authors-page/authors-page').then((m) => m.AuthorsPage),
  },
  ...endpointRoutes,
];
