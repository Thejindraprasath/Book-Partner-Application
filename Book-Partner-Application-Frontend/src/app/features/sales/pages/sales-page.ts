import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

import { SALES_ENDPOINTS, SALES_MODULE, SALES_ROUTE } from '../sales.data';

@Component({
  selector: 'app-sales-page',
  imports: [RouterLink],
  templateUrl: './sales-page.html',
  styleUrl: './sales-page.css',
})
// Module page that lists all sales actions the user can open.
export class SalesPage {
  // Basic details shown in the page header.
  readonly moduleItem = SALES_MODULE;

  // All sales endpoint cards shown on this page.
  readonly endpoints = SALES_ENDPOINTS;

  // Build the route to the selected sales action page.
  getEndpointLink(endpointRoute: string): string {
    return `${SALES_ROUTE}/${endpointRoute}`;
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
