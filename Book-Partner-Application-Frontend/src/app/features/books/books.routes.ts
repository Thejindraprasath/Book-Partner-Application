import { Routes } from '@angular/router';

import { getEndpointsForModule } from '../../config/api.config';

const endpointRoutes: Routes = getEndpointsForModule('book').map((endpoint) => ({
  path: endpoint.route,
  loadComponent: () =>
    import('../../shared/components/endpoint-runner/endpoint-runner').then((m) => m.EndpointRunner),
  data: { moduleId: 'book', moduleRoute: '/book', endpoint },
}));

export const booksRoutes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./pages/books-page/books-page').then((m) => m.BooksPage),
  },
  ...endpointRoutes,
];
