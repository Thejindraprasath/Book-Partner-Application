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
    const body = new HttpParams()
      .set('username', username)
      .set('password', password);

    this.sessionService.clearSession();

    return this.http.post(`${API_BASE_URL}/login`, body.toString(), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      responseType: 'json',
      withCredentials: true,
    }).pipe(
      switchMap(() => this.fetchCurrentUser())
    );
  }

  logout(): Observable<void> {
    return this.http.post<void>(`${API_BASE_URL}/logout`, {}, {
      withCredentials: true,
    }).pipe(
      tap(() => this.sessionService.clearSession())
    );
  }

  fetchCurrentUser(moduleId?: string): Observable<AuthUser> {
    return this.http.get<AuthMeResponse>(`${API_BASE_URL}/auth/me`, {
      withCredentials: true,
    }).pipe(
      map((response) => {
        const resolvedModule = moduleId ?? this.resolveModuleFromRoles(response.roles);
        return {
          username: response.username,
          roles: response.roles,
          authenticated: response.authenticated,
          moduleId: resolvedModule,
        };
      }),
      tap((user) => this.sessionService.setSession(user))
    );
  }

  private resolveModuleFromRoles(roles: string[]): string {
    const moduleItem = APP_MODULES.find((item) =>
      item.roles.every((role) => roles.includes(role))
    ) ?? APP_MODULES.find((item) =>
      item.roles.some((role) => roles.includes(role))
    );

    return moduleItem?.id ?? '';
  }
}
