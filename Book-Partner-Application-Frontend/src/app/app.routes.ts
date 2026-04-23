import { Routes } from '@angular/router';

import { authGuard } from './core/guards/auth.guard';
import { moduleGuard } from './core/guards/module.guard';
import { roleGuard } from './core/guards/role.guard';

export const routes: Routes = [
  {
    path: '',
    loadChildren: () =>
      import('./features/landing/landing.routes').then((m) => m.landingRoutes),
  },
  {
    path: '',
    loadComponent: () =>
      import('./layouts/auth-layout/auth-layout').then((m) => m.AuthLayout),
    children: [
      {
        path: 'login/:moduleId',
        loadChildren: () =>
          import('./features/auth/auth.routes').then((m) => m.authRoutes),
      },
    ],
  },
  {
    path: '',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./layouts/main-layout/main-layout').then((m) => m.MainLayout),
    children: [
      {
        path: 'store',
        canActivate: [moduleGuard, roleGuard],
        data: { moduleId: 'store', roles: ['ROLE_STORE'] },
        loadChildren: () =>
          import('./features/stores/stores.routes').then((m) => m.storesRoutes),
      },
      {
        path: 'author',
        canActivate: [moduleGuard, roleGuard],
        data: { moduleId: 'author', roles: ['ROLE_AUTHOR'] },
        loadChildren: () =>
          import('./features/authors/authors.routes').then((m) => m.authorsRoutes),
      },
      {
        path: 'book',
        canActivate: [moduleGuard, roleGuard],
        data: { moduleId: 'book', roles: ['ROLE_BOOK'] },
        loadChildren: () =>
          import('./features/books/books.routes').then((m) => m.booksRoutes),
      },
      {
        path: 'sanjai',
        canActivate: [moduleGuard],
        data: { moduleId: 'sanjai' },
        loadChildren: () =>
          import('./features/sanjai/sanjai.routes').then((m) => m.sanjaiRoutes),
      },
      {
        path: 'sales',
        canActivate: [moduleGuard, roleGuard],
        data: { moduleId: 'sales', roles: ['ROLE_SALE'] },
        loadChildren: () =>
          import('./features/sales/sales.routes').then((m) => m.salesRoutes),
      },
      {
        path: 'publishers',
        pathMatch: 'full',
        redirectTo: 'sanjai/publishers',
      },
      {
        path: 'employees',
        pathMatch: 'full',
        redirectTo: 'sanjai/employees',
      },
    ],
  },
  {
    path: '**',
    redirectTo: '',
  },
];
