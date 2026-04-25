import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

import { STORES_ENDPOINTS, STORES_MODULE, STORES_ROUTE } from '../stores.data';

@Component({
  selector: 'app-stores-page',
  imports: [RouterLink],
  templateUrl: './stores-page.html',
  styleUrl: './stores-page.css',
})
export class StoresPage {
  // Basic page details for the store module.
  readonly moduleItem = STORES_MODULE;

  // Every endpoint card shown on this page.
  readonly endpoints = STORES_ENDPOINTS;

  getEndpointLink(endpointRoute: string): string {
    return `${STORES_ROUTE}/${endpointRoute}`;
  }

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
