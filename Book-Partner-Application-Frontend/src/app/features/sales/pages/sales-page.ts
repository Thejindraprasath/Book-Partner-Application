import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

import { getEndpointsForModule, getModuleById } from '../../../../config/api.config';

@Component({
  selector: 'app-sales-page',
  imports: [RouterLink],
  templateUrl: './sales-page.html',
  styleUrl: './sales-page.css',
})
export class SalesPage {
  readonly moduleItem = getModuleById('sales');
  readonly endpoints = getEndpointsForModule('sales');

  endpointHref(endpointRoute: string): string {
    return `${this.moduleItem?.route ?? '/sales'}/${endpointRoute}`;
  }
}
