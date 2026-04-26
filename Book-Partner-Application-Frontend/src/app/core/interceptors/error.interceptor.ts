import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

import { SessionService } from '../auth/session.service';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const sessionService = inject(SessionService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      // If the backend says the session is no longer valid, clear local session data
      // and send the user back to the landing page.
      if (error.status === 401 && !req.url.endsWith('/login')) {
        sessionService.clearSession();
        router.navigateByUrl('/');
      }

      return throwError(() => error);
    })
  );
};
