import { Routes } from '@angular/router';

import { BOOKS_ENDPOINTS, BOOKS_MODULE_ID, BOOKS_ROUTE } from './books.data';

const bookEndpointRunner = () =>
  import('../../shared/components/endpoint-runner/endpoint-runner').then(
    (m) => m.EndpointRunner,
  );

// Each endpoint gets its own route so it can open inside the shared runner.
const endpointRoutes: Routes = BOOKS_ENDPOINTS.map((endpoint) => {
  return {
    path: endpoint.route,
    loadComponent: bookEndpointRunner,
    data: {
      moduleId: BOOKS_MODULE_ID,
      moduleRoute: BOOKS_ROUTE,
      endpoint,
    },
  };
});

export const booksRoutes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./pages/books-page').then((m) => m.BooksPage),
  },
  ...endpointRoutes,
];
