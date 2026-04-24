import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

import {
  PUBLISHERS_ENDPOINTS,
  PUBLISHERS_PAGE_DESCRIPTION,
  PUBLISHERS_PAGE_TITLE,
  PUBLISHERS_ROUTE,
} from '../../publishers.data';

@Component({
  selector: 'app-publishers-page',
  imports: [RouterLink],
  templateUrl: './publishers-page.html',
  styleUrl: './publishers-page.css',
})
export class PublishersPage {
  readonly pageTitle = PUBLISHERS_PAGE_TITLE;
  readonly pageDescription = PUBLISHERS_PAGE_DESCRIPTION;

  // Every endpoint card shown on this page.
  readonly endpoints = PUBLISHERS_ENDPOINTS;

  getEndpointLink(endpointRoute: string): string {
    return `${PUBLISHERS_ROUTE}/${endpointRoute}`;
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
