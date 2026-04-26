import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';

import { AuthService } from '../../../core/auth/auth.service';
import { SessionService } from '../../../core/auth/session.service';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
// Top navigation bar shown after login.
export class Navbar {
  private readonly authService = inject(AuthService);
  private readonly sessionService = inject(SessionService);
  private readonly router = inject(Router);

  readonly user = this.sessionService.user;
  readonly homeRoute = '/';

  logout(): void {
    // Try backend logout first. If it fails, still clear the local session and go home.
    this.authService.logout().subscribe({
      next: () => this.router.navigateByUrl('/'),
      error: () => {
        this.sessionService.clearSession();
        this.router.navigateByUrl('/');
      },
    });
  }
}
