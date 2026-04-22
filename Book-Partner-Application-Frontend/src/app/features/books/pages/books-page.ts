import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

import { getEndpointsForModule, getModuleById } from '../../../../config/api.config';

@Component({
  selector: 'app-books-page',
  imports: [RouterLink],
  templateUrl: './books-page.html',
  styleUrl: './books-page.css',
})
export class BooksPage {
  readonly moduleItem = getModuleById('book');
  readonly endpoints = getEndpointsForModule('book');

  endpointHref(endpointRoute: string): string {
    return `${this.moduleItem?.route ?? '/book'}/${endpointRoute}`;
  }
}
