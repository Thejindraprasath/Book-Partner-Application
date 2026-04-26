import { Component, computed, inject } from '@angular/core';
import { RouterLink } from '@angular/router';

import { APP_MODULES, getEndpointsForModule } from '../../../config/api.config';
import { SessionService } from '../../../core/auth/session.service';
import { ModuleDefinition } from '../../../models/module.model';

@Component({
  selector: 'app-landing',
  imports: [RouterLink],
  templateUrl: './landing.html',
  styleUrl: './landing.css',
})
export class Landing {
  private readonly sessionService = inject(SessionService);

  // Cards for every module shown on the landing page.
  readonly modules = APP_MODULES;
  readonly currentModuleId = computed(() => this.sessionService.currentModule());

  loginRoute(moduleItem: ModuleDefinition): string[] {
    return ['/login', moduleItem.id];
  }

  // If the user is already logged in, this points to their current module home page.
  currentModuleRoute(): string[] | null {
    const moduleId = this.currentModuleId();

    if (!moduleId) {
      return null;
    }

    return ['/', moduleId];
  }

  endpointCount(moduleId: string): number {
    // Sanjai contains two nested endpoint groups, so its total is combined here.
    if (moduleId === 'sanjai') {
      return getEndpointsForModule('publisher').length + getEndpointsForModule('employee').length;
    }

    return getEndpointsForModule(moduleId).length;
  }

  moduleIcon(moduleId: string): string {
    switch (moduleId) {
      case 'store':
        return '🏬';
      case 'author':
        return '✍️';
      case 'book':
        return '📚';
      case 'sanjai':
        return '🏢';
      case 'sales':
        return '📈';
      default:
        return '📌';
    }
  }

  moduleStatLabel(moduleId: string): string {
    switch (moduleId) {
      case 'store':
        return 'Store & Discount APIs';
      case 'author':
        return 'Author APIs';
      case 'book':
        return 'Book & Royalty APIs';
      case 'sanjai':
        return 'Publisher & Employee APIs';
      case 'sales':
        return 'Sales APIs';
      default:
        return 'Available APIs';
    }
  }
}
