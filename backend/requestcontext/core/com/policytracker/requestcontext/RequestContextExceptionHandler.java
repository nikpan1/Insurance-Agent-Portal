package com.policytracker.requestcontext;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RequestContextExceptionHandler {

    @ExceptionHandler(MissingOrInvalidUserIdException.class)
    public ProblemDetail handleMissingOrInvalidUserId(MissingOrInvalidUserIdException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Invalid request context");
        problemDetail.setDetail(ex.getMessage());
        return problemDetail;
    }
}
