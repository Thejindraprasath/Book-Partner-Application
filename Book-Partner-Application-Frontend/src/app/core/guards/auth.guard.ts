import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { catchError, map, of } from 'rxjs';

import { AuthService } from '../auth/auth.service';
import { SessionService } from '../auth/session.service';

export const authGuard: CanActivateFn = () => {
  const sessionService = inject(SessionService);
  const authService = inject(AuthService);
  const router = inject(Router);

  // If login data is already in session storage, allow the route immediately.
  if (sessionService.isLoggedIn()) {
    return true;
  }

  // If the page is refreshed, ask the backend for the current user.
  // If that fails, send the user back to the landing page.
  return authService.fetchCurrentUser().pipe(
    map(() => true),
    catchError(() => of(router.createUrlTree(['/'])))
  );
};
