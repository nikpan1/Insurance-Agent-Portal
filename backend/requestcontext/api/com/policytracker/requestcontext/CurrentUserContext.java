package com.policytracker.requestcontext;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Getter
@Setter
@NoArgsConstructor
@Component
@RequestScope
public class CurrentUserContext {

    private UserId userId;
}
