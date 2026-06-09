import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';

import { RequestContextService } from '../request-context/request-context.service';

export const requestContextInterceptor: HttpInterceptorFn = (request, next) => {
  if (!request.url.startsWith('/api/')) {
    return next(request);
  }

  const userId = inject(RequestContextService).currentUserId();

  return next(
    request.clone({
      setHeaders: {
        'X-User-Id': String(userId)
      }
    })
  );
};
