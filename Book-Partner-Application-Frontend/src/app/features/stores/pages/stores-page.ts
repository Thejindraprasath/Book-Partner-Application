import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

import { getEndpointsForModule, getModuleById } from '../../../../config/api.config';

@Component({
  selector: 'app-stores-page',
  imports: [RouterLink],
  templateUrl: './stores-page.html',
  styleUrl: './stores-page.css',
})
export class StoresPage {
  readonly moduleItem = getModuleById('store');
  readonly endpoints = getEndpointsForModule('store');

  endpointHref(endpointRoute: string): string {
    return `${this.moduleItem?.route ?? '/store'}/${endpointRoute}`;
  }
}
