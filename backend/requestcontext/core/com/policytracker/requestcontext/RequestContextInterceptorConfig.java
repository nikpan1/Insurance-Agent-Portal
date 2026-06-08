package com.policytracker.requestcontext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.MappedInterceptor;

@Configuration
public class RequestContextInterceptorConfig {

    @Bean
    public MappedInterceptor currentUserContextMappedInterceptor(CurrentUserContextInterceptor interceptor) {
        return new MappedInterceptor(new String[]{"/api/**"}, interceptor);
    }
}
