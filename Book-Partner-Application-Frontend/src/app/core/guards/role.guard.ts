import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

import { SessionService } from '../auth/session.service';

export const roleGuard: CanActivateFn = (route) => {
  const sessionService = inject(SessionService);
  const router = inject(Router);
  const allowedRoles = (route.data?.['roles'] as string[] | undefined) ?? [];

  if (allowedRoles.length === 0) {
    return true;
  }

  const hasAccess = allowedRoles.some((role) => sessionService.hasRole(role));
  return hasAccess ? true : router.createUrlTree(['/']);
};
