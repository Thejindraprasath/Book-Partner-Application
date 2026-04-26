import { HttpInterceptorFn } from '@angular/common/http';

import { API_BASE_URL } from '../../config/api.config';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  // If the request URL is relative, attach the API base URL first.
  const requestUrl = req.url.startsWith('http') ? req.url : `${API_BASE_URL}${req.url}`;

  // Always send cookies with API requests so the backend can identify the session.
  const clonedRequest = req.clone({
    url: requestUrl,
    withCredentials: true,
  });

  return next(clonedRequest);
};
