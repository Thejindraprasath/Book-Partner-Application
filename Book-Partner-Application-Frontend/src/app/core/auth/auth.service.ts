import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, map, switchMap, tap } from 'rxjs';

import { API_BASE_URL, APP_MODULES } from '../../config/api.config';
import { AuthUser } from '../../models/user.model';
import { SessionService } from './session.service';

interface AuthMeResponse {
  username: string;
  roles: string[];
  authenticated: boolean;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly sessionService = inject(SessionService);

  login(moduleId: string, username: string, password: string): Observable<AuthUser> {
    // Spring Security expects login fields as form data.
    const body = new HttpParams()
      .set('username', username)
      .set('password', password);

    // Remove any old session before starting a new login.
    this.sessionService.clearSession();

    return this.http.post(`${API_BASE_URL}/login`, body.toString(), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      responseType: 'json',
      withCredentials: true,
    }).pipe(
      // After login succeeds, load the full user profile and store it in session.
      switchMap(() => this.fetchCurrentUser())
    );
  }

  logout(): Observable<void> {
    return this.http.post<void>(`${API_BASE_URL}/logout`, {}, {
      withCredentials: true,
    }).pipe(
      // Clear local session data after backend logout succeeds.
      tap(() => this.sessionService.clearSession())
    );
  }

  fetchCurrentUser(moduleId?: string): Observable<AuthUser> {
    return this.http.get<AuthMeResponse>(`${API_BASE_URL}/auth/me`, {
      withCredentials: true,
    }).pipe(
      map((response) => {
        // Pick the active module from the explicit route/module or infer it from roles.
        const resolvedModule = moduleId ?? this.resolveModuleFromRoles(response.roles);
        return {
          username: response.username,
          roles: response.roles,
          authenticated: response.authenticated,
          moduleId: resolvedModule,
        };
      }),
      // Save the latest user data so guards and UI can use it everywhere.
      tap((user) => this.sessionService.setSession(user))
    );
  }

  private resolveModuleFromRoles(roles: string[]): string {
    // First try to find a module where all required roles are present.
    // If that fails, fall back to the first module matching at least one role.
    const moduleItem = APP_MODULES.find((item) =>
      item.roles.every((role) => roles.includes(role))
    ) ?? APP_MODULES.find((item) =>
      item.roles.some((role) => roles.includes(role))
    );

    return moduleItem?.id ?? '';
  }
}
