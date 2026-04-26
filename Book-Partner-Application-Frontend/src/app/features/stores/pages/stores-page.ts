import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

import { STORES_ENDPOINTS, STORES_MODULE, STORES_ROUTE } from '../stores.data';

@Component({
  selector: 'app-stores-page',
  imports: [RouterLink],
  templateUrl: './stores-page.html',
  styleUrl: './stores-page.css',
})
// Module page that lists all store actions the user can open.
export class StoresPage {
  // Basic details shown in the page header.
  readonly moduleItem = STORES_MODULE;

  // All store endpoint cards shown on this page.
  readonly endpoints = STORES_ENDPOINTS;

  // Build the route to the selected store action page.
  getEndpointLink(endpointRoute: string): string {
    return `${STORES_ROUTE}/${endpointRoute}`;
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
