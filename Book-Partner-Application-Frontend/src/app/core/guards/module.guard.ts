import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

import { SessionService } from '../auth/session.service';

export const moduleGuard: CanActivateFn = (route) => {
  const sessionService = inject(SessionService);
  const router = inject(Router);
  const moduleId = route.data?.['moduleId'] as string | undefined;

  if (!moduleId) {
    return true;
  }

  return sessionService.currentModule() === moduleId ? true : router.createUrlTree(['/']);
};
