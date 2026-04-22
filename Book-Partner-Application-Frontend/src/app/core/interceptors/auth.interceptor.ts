import { HttpInterceptorFn } from '@angular/common/http';

import { API_BASE_URL } from '../../config/api.config';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const requestUrl = req.url.startsWith('http') ? req.url : `${API_BASE_URL}${req.url}`;
  const clonedRequest = req.clone({
    url: requestUrl,
    withCredentials: true,
  });

  return next(clonedRequest);
};
