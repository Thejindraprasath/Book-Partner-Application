import { Component } from '@angular/core';

import { getEndpointsForModule, getModuleById } from '../../../../config/api.config';

@Component({
  selector: 'app-publishers-page',
  templateUrl: './publishers-page.html',
  styleUrl: './publishers-page.css',
})
export class PublishersPage {
  readonly moduleItem = getModuleById('sanjai');
  readonly endpoints = getEndpointsForModule('publisher');

  endpointHref(endpointRoute: string): string {
    return `/sanjai/publishers/${endpointRoute}`;
  }
}
