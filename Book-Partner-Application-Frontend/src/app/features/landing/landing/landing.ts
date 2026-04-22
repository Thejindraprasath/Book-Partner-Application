import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';

import { APP_MODULES } from '../../../config/api.config';
import { SessionService } from '../../../core/auth/session.service';

@Component({
  selector: 'app-landing',
  imports: [RouterLink],
  templateUrl: './landing.html',
  styleUrl: './landing.css',
})
export class Landing {
  private readonly sessionService = inject(SessionService);

  readonly modules = APP_MODULES;
  readonly currentModule = this.sessionService.currentModule();
}
