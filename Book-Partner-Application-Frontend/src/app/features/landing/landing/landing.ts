import { Component, computed, inject } from '@angular/core';
import { RouterLink } from '@angular/router';

import { APP_MODULES } from '../../../config/api.config';
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

  readonly modules = APP_MODULES;
  readonly currentModuleId = computed(() => this.sessionService.currentModule());

  loginRoute(moduleItem: ModuleDefinition): string[] {
    return ['/login', moduleItem.id];
  }

  currentModuleRoute(): string[] | null {
    const moduleId = this.currentModuleId();

    if (!moduleId) {
      return null;
    }

    return ['/', moduleId];
  }
}
