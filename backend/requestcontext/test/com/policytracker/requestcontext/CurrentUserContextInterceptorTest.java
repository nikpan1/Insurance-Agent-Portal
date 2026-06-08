package com.policytracker.requestcontext;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class CurrentUserContextInterceptorTest {

    @Test
    void preHandleStoresUserIdFromHeader() {
        CurrentUserContext context = new CurrentUserContext();
        CurrentUserContextInterceptor interceptor = new CurrentUserContextInterceptor(context);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-User-Id", "123");

        boolean proceed = interceptor.preHandle(request, new MockHttpServletResponse(), new Object());

        assertThat(proceed).isTrue();
        assertThat(context.getUserId()).isEqualTo(new UserId(123));
    }
}
