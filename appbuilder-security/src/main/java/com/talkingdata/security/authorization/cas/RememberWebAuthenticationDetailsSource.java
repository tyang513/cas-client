package com.talkingdata.security.authorization.cas;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ruobin.yang
 */
public class RememberWebAuthenticationDetailsSource implements
        AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {

    @Override
    public WebAuthenticationDetails buildDetails(HttpServletRequest request) {
        return new RememberWebAuthenticationDetails(request);
    }
}
