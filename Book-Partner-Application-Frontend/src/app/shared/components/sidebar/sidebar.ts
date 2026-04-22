import { CommonModule } from '@angular/common';
import { Component, computed, inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { filter, map, startWith } from 'rxjs';

import { APP_MODULES, getEndpointsForModule } from '../../../config/api.config';
import { SessionService } from '../../../core/auth/session.service';

@Component({
  selector: 'app-sidebar',
  imports: [CommonModule, RouterLink],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css',
})
export class Sidebar {
  private readonly sessionService = inject(SessionService);
  private readonly router = inject(Router);
  private readonly currentUrlSignal = toSignal(
    this.router.events.pipe(
      filter((event): event is NavigationEnd => event instanceof NavigationEnd),
      map((event) => event.urlAfterRedirects),
      startWith(this.router.url)
    ),
    { initialValue: this.router.url }
  );

  readonly currentModule = computed(() => this.sessionService.currentModule());
  readonly homeRoute = '/';
  readonly currentModuleDetails = computed(() =>
    APP_MODULES.find((moduleItem) => moduleItem.id === this.currentModule())
  );
  readonly activeSanjaiSection = computed(() => {
    const currentUrl = this.currentUrlSignal();

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
  });
  readonly currentEndpoints = computed(() => {
    const moduleId = this.currentModule();
    if (!moduleId) {
      return [];
    }

    if (moduleId === 'sanjai') {
      return [];
    }

    return getEndpointsForModule(moduleId);
  });

  endpointRoute(moduleRoute: string, endpointRoute: string): string {
    return `${moduleRoute}/${endpointRoute}`;
  }

  isRouteActiveOrInside(route: string): boolean {
    const currentUrl = this.currentUrlSignal();

    return currentUrl === route || currentUrl.startsWith(`${route}/`);
  }

  openRoute(route: string): string {
    return route;
  }

  isRouteActive(route: string): boolean {
    return this.currentUrlSignal() === route;
  }
}
