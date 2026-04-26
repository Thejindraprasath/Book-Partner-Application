import { Routes } from '@angular/router';

import { BOOKS_ENDPOINTS, BOOKS_MODULE_ID, BOOKS_ROUTE } from './books.data';

const bookEndpointRunner = () =>
  import('../../shared/components/endpoint-runner/endpoint-runner').then(
    (m) => m.EndpointRunner,
  );

// Build one child route per book action so the shared endpoint runner can load it by URL.
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
  // Main page for the book module.
  {
    path: '',
    loadComponent: () =>
      import('./pages/books-page').then((m) => m.BooksPage),
  },
  // Action pages like add, update, delete, and search are added here automatically.
  ...endpointRoutes,
];
