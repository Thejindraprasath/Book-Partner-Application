import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

import { SessionService } from '../auth/session.service';

export const moduleGuard: CanActivateFn = (route) => {
  const sessionService = inject(SessionService);
  const router = inject(Router);
  const moduleId = route.data?.['moduleId'] as string | undefined;

  // If the route does not ask for a module check, allow it.
  if (!moduleId) {
    return true;
  }

  // Only allow the route when the logged-in user belongs to the required module.
  return sessionService.currentModule() === moduleId ? true : router.createUrlTree(['/']);
};
