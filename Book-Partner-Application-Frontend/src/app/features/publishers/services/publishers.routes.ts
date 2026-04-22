import { Routes } from '@angular/router';

import { getEndpointsForModule } from '../../config/api.config';

const endpointRoutes: Routes = getEndpointsForModule('publisher').map((endpoint) => ({
  path: endpoint.route,
  loadComponent: () =>
    import('../../shared/components/endpoint-runner/endpoint-runner').then((m) => m.EndpointRunner),
  data: { moduleId: 'publisher', moduleRoute: '/sanjai/publishers', endpoint },
}));

export const publishersRoutes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./pages/publishers-page/publishers-page').then((m) => m.PublishersPage),
  },
  ...endpointRoutes,
];
