import { Routes } from '@angular/router';

import { authGuard } from './core/guards/auth.guard';
import { moduleGuard } from './core/guards/module.guard';
import { roleGuard } from './core/guards/role.guard';

export const routes: Routes = [
  // Group 1: open the landing page when the user visits the root URL.
  {
    path: '',
    loadChildren: () =>
      import('./features/landing/landing/landing.routes').then(
        (m) => m.landingRoutes
      ),
  },
  // Group 2: show login pages inside the auth layout.
  {
    path: '',
    loadComponent: () =>
      import('./layouts/auth-layout/auth-layout').then((m) => m.AuthLayout),
    children: [
      {
        path: 'login/:moduleId',
        loadChildren: () =>
          import('./features/auth/auth.route').then((m) => m.authRoutes),
      },
    ],
  },
  // Group 3: all business modules live inside the main layout.
  // The user must be logged in before opening any route in this group.
  {
    path: '',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./layouts/main-layout/main-layout').then((m) => m.MainLayout),
    children: [
      // Store pages: only for users who belong to the store module and have store role access.
      {
        path: 'store',
        canActivate: [moduleGuard, roleGuard],
        data: { moduleId: 'store', roles: ['ROLE_STORE'] },
        loadChildren: () =>
          import('./features/stores/stores.routes').then((m) => m.storesRoutes),
      },
      // Author pages: only for users who belong to the author module and have author role access.
      {
        path: 'author',
        canActivate: [moduleGuard, roleGuard],
        data: { moduleId: 'author', roles: ['ROLE_AUTHOR'] },
        loadChildren: () =>
          import('./features/authors/authors.route').then(
            (m) => m.authorsRoutes
          ),
      },
      // Book pages: only for users who belong to the book module and have book role access.
      {
        path: 'book',
        canActivate: [moduleGuard, roleGuard],
        data: { moduleId: 'book', roles: ['ROLE_BOOK'] },
        loadChildren: () =>
          import('./features/books/books.routes').then((m) => m.booksRoutes),
      },
      // Sanjai pages: first check the module, then let child routes check more specific roles.
      {
        path: 'sanjai',
        canActivate: [moduleGuard],
        data: { moduleId: 'sanjai' },
        loadChildren: () =>
          import('./features/sanjai/sanjai.routes').then(
            (m) => m.sanjaiRoutes
          ),
      },
      // Sales pages: only for users who belong to the sales module and have sales role access.
      {
        path: 'sales',
        canActivate: [moduleGuard, roleGuard],
        data: { moduleId: 'sales', roles: ['ROLE_SALE'] },
        loadChildren: () =>
          import('./features/sales/sales.routes').then((m) => m.salesRoutes),
      },
      // Old publishers URL: send it to the new nested Sanjai publishers route.
      {
        path: 'publishers',
        pathMatch: 'full',
        redirectTo: 'sanjai/publishers',
      },
      // Old employees URL: send it to the new nested Sanjai employees route.
      {
        path: 'employees',
        pathMatch: 'full',
        redirectTo: 'sanjai/employees',
      },
    ],
  },
  // If the URL does not match anything, send the user back to the landing page.
  {
    path: '**',
    redirectTo: '',
  },
];
