import { Injectable, signal } from '@angular/core';

import { AuthUser } from '../../models/user.model';

const SESSION_STORAGE_KEY = 'book-partner-session';

@Injectable({
  providedIn: 'root',
})
export class SessionService {
  private readonly sessionState = signal<AuthUser | null>(this.readStoredSession());

  readonly user = this.sessionState.asReadonly();

  setSession(user: AuthUser): void {
    this.sessionState.set(user);
    sessionStorage.setItem(SESSION_STORAGE_KEY, JSON.stringify(user));
  }

  clearSession(): void {
    this.sessionState.set(null);
    sessionStorage.removeItem(SESSION_STORAGE_KEY);
  }

  hasRole(role: string): boolean {
    return this.sessionState()?.roles.includes(role) ?? false;
  }

  isLoggedIn(): boolean {
    return this.sessionState()?.authenticated ?? false;
  }

  currentModule(): string | null {
    return this.sessionState()?.moduleId ?? null;
  }

  private readStoredSession(): AuthUser | null {
    const stored = sessionStorage.getItem(SESSION_STORAGE_KEY);
    if (!stored) {
      return null;
    }

    try {
      return JSON.parse(stored) as AuthUser;
    } catch {
      sessionStorage.removeItem(SESSION_STORAGE_KEY);
      return null;
    }
  }
}
