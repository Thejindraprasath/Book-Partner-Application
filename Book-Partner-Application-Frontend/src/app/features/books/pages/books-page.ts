import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

import { BOOKS_ENDPOINTS, BOOKS_MODULE, BOOKS_ROUTE } from '../books.data';

@Component({
  selector: 'app-books-page',
  imports: [RouterLink],
  templateUrl: './books-page.html',
  styleUrl: './books-page.css',
})
// Module page that lists all book actions the user can open.
export class BooksPage {
  // Basic details shown in the page header.
  readonly moduleItem = BOOKS_MODULE;

  // All book endpoint cards shown on this page.
  readonly endpoints = BOOKS_ENDPOINTS;

  // Build the route to the selected book action page.
  getEndpointLink(endpointRoute: string): string {
    return `${BOOKS_ROUTE}/${endpointRoute}`;
  }

  // Return a color based on the HTTP method so actions are easier to scan.
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
