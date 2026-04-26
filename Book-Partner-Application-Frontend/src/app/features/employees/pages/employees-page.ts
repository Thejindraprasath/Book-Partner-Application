import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

import {
  EMPLOYEES_ENDPOINTS,
  EMPLOYEES_PAGE_DESCRIPTION,
  EMPLOYEES_PAGE_TITLE,
  EMPLOYEES_ROUTE,
} from '../employees.data';

@Component({
  selector: 'app-employees-page',
  imports: [RouterLink],
  templateUrl: './employees-page.html',
  styleUrl: './employees-page.css',
})
// Module page that lists all employee and job actions inside Sanjai.
export class EmployeesPage {
  // Text shown in the page header.
  readonly pageTitle = EMPLOYEES_PAGE_TITLE;
  readonly pageDescription = EMPLOYEES_PAGE_DESCRIPTION;

  // All employee endpoint cards shown on this page.
  readonly endpoints = EMPLOYEES_ENDPOINTS;

  // Build the route to the selected employee action page.
  getEndpointLink(endpointRoute: string): string {
    return `${EMPLOYEES_ROUTE}/${endpointRoute}`;
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
