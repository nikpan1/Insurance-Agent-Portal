package com.policytracker.requestcontext;

public record UserId(long value) {

    public UserId {
        if (value <= 0) {
            throw new MissingOrInvalidUserIdException("Invalid X-User-Id header value. Expected a positive number.");
        }
    }

    public static UserId fromHeader(String headerValue) {
        if (headerValue == null || headerValue.isBlank()) {
            throw new MissingOrInvalidUserIdException("Missing required header: X-User-Id");
        }

        try {
            return new UserId(Long.parseLong(headerValue));
        } catch (NumberFormatException ex) {
            throw new MissingOrInvalidUserIdException("Invalid X-User-Id header value. Expected a positive number.");
        }
    }
}
