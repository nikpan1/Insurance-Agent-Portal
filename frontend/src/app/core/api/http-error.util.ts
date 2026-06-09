import { HttpErrorResponse } from '@angular/common/http';

import { ProblemDetail } from './api.types';

export function toProblemDetail(error: unknown): ProblemDetail {
  if (error instanceof HttpErrorResponse) {
    const payload = error.error;

    if (isProblemDetail(payload)) {
      return payload;
    }

    if (typeof payload === 'string' && payload.trim().length > 0) {
      return {
        title: 'Request failed',
        status: error.status,
        detail: payload
      };
    }

    return {
      title: 'Request failed',
      status: error.status,
      detail: error.message
    };
  }

  return {
    title: 'Unexpected error',
    detail: 'Unexpected error while contacting the API.'
  };
}

function isProblemDetail(value: unknown): value is ProblemDetail {
  if (!value || typeof value !== 'object') {
    return false;
  }

  const candidate = value as ProblemDetail;

  return (
    typeof candidate.title === 'string' ||
    typeof candidate.detail === 'string' ||
    typeof candidate.status === 'number'
  );
}
