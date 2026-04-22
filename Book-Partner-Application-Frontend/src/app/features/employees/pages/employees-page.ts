import { Component } from '@angular/core';

import { getEndpointsForModule, getModuleById } from '../../../../config/api.config';

@Component({
  selector: 'app-employees-page',
  templateUrl: './employees-page.html',
  styleUrl: './employees-page.css',
})
export class EmployeesPage {
  readonly moduleItem = getModuleById('sanjai');
  readonly endpoints = getEndpointsForModule('employee');

  endpointHref(endpointRoute: string): string {
    return `/sanjai/employees/${endpointRoute}`;
  }
}
