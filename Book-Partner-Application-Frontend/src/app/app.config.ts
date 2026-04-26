import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';

import { routes } from './app.routes';
import { authInterceptor } from './core/interceptors/auth.interceptor';
import { errorInterceptor } from './core/interceptors/error.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    // Catch browser-level runtime errors in one central place.
    provideBrowserGlobalErrorListeners(),
    // Register the full app route list.
    provideRouter(routes),
    // Run the shared request/response interceptors for every HTTP call.
    provideHttpClient(withInterceptors([authInterceptor, errorInterceptor]))
  ]
};
