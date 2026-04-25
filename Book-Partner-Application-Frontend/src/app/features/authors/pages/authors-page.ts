import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

import { AUTHORS_ENDPOINTS, AUTHORS_MODULE, AUTHORS_ROUTE } from '../authors.data';

@Component({
  selector: 'app-authors-page',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './authors-page.html',
  styleUrl: './authors-page.css',
})
export class AuthorsPage {
  // Module details
  readonly moduleItem = AUTHORS_MODULE;

  // Endpoints list
  readonly endpoints = AUTHORS_ENDPOINTS;

  // Generate route link
  getEndpointLink(endpointRoute: string): string {
    return `${AUTHORS_ROUTE}/${endpointRoute}`;
  }

  // Badge styling based on method
  getMethodBadgeClass(method: string): string {
    switch (method) {
      case 'POST':
        return 'inline-flex min-w-20 items-center justify-center rounded-2xl bg-emerald-500 px-4 py-2 text-sm font-bold tracking-wide text-white shadow-sm';
      case 'PUT':
        return 'inline-flex min-w-20 items-center justify-center rounded-2xl bg-amber-500 px-4 py-2 text-sm font-bold tracking-wide text-white shadow-sm';
      case 'DELETE':
        return 'inline-flex min-w-20 items-center justify-center rounded-2xl bg-rose-500 px-4 py-2 text-sm font-bold tracking-wide text-white shadow-sm';
      default:
        return 'inline-flex min-w-20 items-center justify-center rounded-2xl bg-sky-500 px-4 py-2 text-sm font-bold tracking-wide text-white shadow-sm';
    }
  }
}
