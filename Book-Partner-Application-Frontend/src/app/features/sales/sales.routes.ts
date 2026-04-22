import { Routes } from '@angular/router';

import { getEndpointsForModule } from '../../config/api.config';

const endpointRoutes: Routes = getEndpointsForModule('sales').map((endpoint) => ({
  path: endpoint.route,
  loadComponent: () =>
    import('../../shared/components/endpoint-runner/endpoint-runner').then((m) => m.EndpointRunner),
  data: { moduleId: 'sales', moduleRoute: '/sales', endpoint },
}));

export const salesRoutes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./pages/sales-page/sales-page').then((m) => m.SalesPage),
  },
  ...endpointRoutes,
];
