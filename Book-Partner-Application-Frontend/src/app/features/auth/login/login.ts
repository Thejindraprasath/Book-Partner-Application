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
    this.errorMessage.set(
      error.error?.message ?? 'Login failed. Please check your username and password.',
    );
    this.isLoading.set(false);
  }
}
