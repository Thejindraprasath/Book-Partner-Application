import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

import { SessionService } from '../auth/session.service';

export const roleGuard: CanActivateFn = (route) => {
  const sessionService = inject(SessionService);
  const router = inject(Router);
  const allowedRoles = (route.data?.['roles'] as string[] | undefined) ?? [];

  // If the route does not list any roles, allow it.
  if (allowedRoles.length === 0) {
    return true;
  }

  // Allow the route when the user has at least one of the required roles.
  const hasAccess = allowedRoles.some((role) => sessionService.hasRole(role));
  return hasAccess ? true : router.createUrlTree(['/']);
};
