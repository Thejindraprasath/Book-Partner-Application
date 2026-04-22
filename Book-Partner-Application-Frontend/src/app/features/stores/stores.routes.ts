import { Routes } from '@angular/router';

import { getEndpointsForModule } from '../../config/api.config';

const endpointRoutes: Routes = getEndpointsForModule('store').map((endpoint) => ({
  path: endpoint.route,
  loadComponent: () =>
    import('../../shared/components/endpoint-runner/endpoint-runner').then((m) => m.EndpointRunner),
  data: { moduleId: 'store', moduleRoute: '/store', endpoint },
}));

export const storesRoutes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./pages/stores-page/stores-page').then((m) => m.StoresPage),
  },
  ...endpointRoutes,
];
