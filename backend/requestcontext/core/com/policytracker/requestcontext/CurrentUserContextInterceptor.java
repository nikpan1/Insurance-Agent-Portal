package com.policytracker.requestcontext;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class CurrentUserContextInterceptor implements HandlerInterceptor {

    private static final String USER_ID_HEADER = "X-User-Id";

    private final CurrentUserContext currentUserContext;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        final String userIdHeaderValue = request.getHeader(USER_ID_HEADER);
        final UserId userId = UserId.fromHeader(userIdHeaderValue);
        currentUserContext.setUserId(userId);
        return true;
    }
}
