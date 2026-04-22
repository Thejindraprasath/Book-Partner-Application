import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { catchError, map, of } from 'rxjs';

import { AuthService } from '../auth/auth.service';
import { SessionService } from '../auth/session.service';

export const authGuard: CanActivateFn = () => {
  const sessionService = inject(SessionService);
  const authService = inject(AuthService);
  const router = inject(Router);

  if (sessionService.isLoggedIn()) {
    return true;
  }

  return authService.fetchCurrentUser().pipe(
    map(() => true),
    catchError(() => of(router.createUrlTree(['/'])))
  );
};
