import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

import {
  PUBLISHERS_ENDPOINTS,
  PUBLISHERS_PAGE_DESCRIPTION,
  PUBLISHERS_PAGE_TITLE,
  PUBLISHERS_ROUTE,
} from '../publishers.data';

@Component({
  selector: 'app-publishers-page',
  imports: [RouterLink],
  templateUrl: './publishers-page.html',
  styleUrl: './publishers-page.css',
})
// Module page that lists all publisher actions inside Sanjai.
export class PublishersPage {
  // Text shown in the page header.
  readonly pageTitle = PUBLISHERS_PAGE_TITLE;
  readonly pageDescription = PUBLISHERS_PAGE_DESCRIPTION;

  // All publisher endpoint cards shown on this page.
  readonly endpoints = PUBLISHERS_ENDPOINTS;

  // Build the route to the selected publisher action page.
  getEndpointLink(endpointRoute: string): string {
    return `${PUBLISHERS_ROUTE}/${endpointRoute}`;
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
