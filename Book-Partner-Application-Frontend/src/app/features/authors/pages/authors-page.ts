import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

import { getEndpointsForModule, getModuleById } from '../../../../config/api.config';

@Component({
  selector: 'app-authors-page',
  imports: [RouterLink],
  templateUrl: './authors-page.html',
  styleUrl: './authors-page.css',
})
export class AuthorsPage {
  readonly moduleItem = getModuleById('author');
  readonly endpoints = getEndpointsForModule('author');

  endpointHref(endpointRoute: string): string {
    return `${this.moduleItem?.route ?? '/author'}/${endpointRoute}`;
  }
}
