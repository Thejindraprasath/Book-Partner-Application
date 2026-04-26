import { Component, computed, inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { filter, map, startWith } from 'rxjs';

import { APP_MODULES, getEndpointsForModule } from '../../../config/api.config';
import { SessionService } from '../../../core/auth/session.service';
import { EndpointDefinition, ModuleDefinition } from '../../../models/module.model';

interface SidebarSection {
  label: string;
  route: string;
  endpoints: EndpointDefinition[];
}

@Component({
  selector: 'app-sidebar',
  imports: [RouterLink],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css',
})
// Sidebar that shows the current module and its available endpoints.
export class Sidebar {
  private readonly sessionService = inject(SessionService);
  private readonly router = inject(Router);

  private readonly currentUrlSignal = toSignal(
    this.router.events.pipe(
      filter((event): event is NavigationEnd => event instanceof NavigationEnd),
      map((event) => event.urlAfterRedirects),
      startWith(this.router.url),
    ),
    { initialValue: this.router.url },
  );

  readonly homeRoute = '/';
  readonly currentModuleId = computed(() => this.sessionService.currentModule());
  readonly currentModuleDetails = computed(() => this.findCurrentModule());
  readonly currentEndpoints = computed(() => this.getCurrentEndpoints());
  readonly activeSanjaiSection = computed(() => this.getActiveSanjaiSection());

  private findCurrentModule(): ModuleDefinition | undefined {
    return APP_MODULES.find((moduleItem) => moduleItem.id === this.currentModuleId());
  }

  private getCurrentEndpoints(): EndpointDefinition[] {
    const moduleId = this.currentModuleId();

    // Sanjai does not show one flat list because its endpoints are split by section.
    if (!moduleId || moduleId === 'sanjai') {
      return [];
    }

    return getEndpointsForModule(moduleId);
  }

  private getActiveSanjaiSection(): SidebarSection | null {
    const currentUrl = this.currentUrlSignal();

    // Decide which nested Sanjai section is active from the current URL.
    if (currentUrl.startsWith('/sanjai/publishers')) {
      return {
        label: 'Publisher Endpoints',
        route: '/sanjai/publishers',
        endpoints: getEndpointsForModule('publisher'),
      };
    }

    if (currentUrl.startsWith('/sanjai/employees')) {
      return {
        label: 'Employee Endpoints',
        route: '/sanjai/employees',
        endpoints: getEndpointsForModule('employee'),
      };
    }

    return null;
  }

  endpointRoute(moduleRoute: string, endpointRoute: string): string {
    return `${moduleRoute}/${endpointRoute}`;
  }

  isRouteActive(route: string): boolean {
    return this.currentUrlSignal() === route;
  }

  isRouteActiveOrInside(route: string): boolean {
    // Treat child pages as active too, so parent menu items stay highlighted.
    const currentUrl = this.currentUrlSignal();
    return currentUrl === route || currentUrl.startsWith(`${route}/`);
  }
}
