import { HttpErrorResponse } from '@angular/common/http';
import { Component, computed, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';

import { getModuleById } from '../../../config/api.config';
import { AuthService } from '../../../core/auth/auth.service';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  private readonly formBuilder = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  readonly moduleId = this.route.snapshot.paramMap.get('moduleId') ?? '';
  readonly moduleItem = getModuleById(this.moduleId);

  // Simple login form shared by all modules.
  readonly form = this.formBuilder.group({
    username: ['', Validators.required],
    password: ['', Validators.required],
  });

  readonly errorMessage = signal('');
  readonly isLoading = signal(false);
  readonly submitButtonText = computed(() => (this.isLoading() ? 'Signing in...' : 'Login'));

  submit(): void {
    this.errorMessage.set('');

    if (this.form.invalid || !this.moduleItem) {
      this.form.markAllAsTouched();
      return;
    }

    const username = this.form.get('username')?.value ?? '';
    const password = this.form.get('password')?.value ?? '';

    // Try to log in, then verify that the user belongs to the selected module.
    this.isLoading.set(true);
    this.authService.login(this.moduleId, username, password).subscribe({
      next: (user) => this.handleLoginSuccess(user.roles),
      error: (error: HttpErrorResponse) => this.handleLoginError(error),
    });
  }

  private handleLoginSuccess(userRoles: string[]): void {
    if (!this.moduleItem) {
      this.isLoading.set(false);
      return;
    }

    // The same backend session can hold different roles, so make sure
    // the user entered this module through the correct login card.
    const hasModuleAccess = this.moduleItem.roles.some((role) => userRoles.includes(role));

    if (!hasModuleAccess) {
      this.handleWrongModuleLogin();
      return;
    }

    this.isLoading.set(false);
    this.router.navigateByUrl(this.moduleItem.route);
  }

  private handleWrongModuleLogin(): void {
    const moduleLabel = this.moduleItem?.label ?? 'this module';

    // Immediately log out again so the wrong module session is not kept open.
    this.authService.logout().subscribe({
      next: () => {
        this.errorMessage.set(`Use the correct credentials for ${moduleLabel}.`);
        this.isLoading.set(false);
      },
      error: () => {
        this.errorMessage.set(`Use the correct credentials for ${moduleLabel}.`);
        this.isLoading.set(false);
      },
    });
  }

  private handleLoginError(error: HttpErrorResponse): void {
    // Show the backend message when available, otherwise use a simple fallback.
    this.errorMessage.set(
      error.error?.message ?? 'Login failed. Please check your username and password.',
    );
    this.isLoading.set(false);
  }
}
