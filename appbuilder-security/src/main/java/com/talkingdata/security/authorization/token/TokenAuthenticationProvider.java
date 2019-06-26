package com.talkingdata.security.authorization.token;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

/**
 * 令牌认证
 * @author tao.yang
 * @date 2019-06-26
 */
public class TokenAuthenticationProvider implements AuthenticationProvider{

    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationProvider.class);

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // 如果认证通过，则不再认证
        if (authentication.isAuthenticated()) {
            return authentication;
        }

        String token = authentication.getCredentials().toString();
        if (token == null || "".equals(token)){
            return authentication;
        }

        // 这里验证token
        User user = (User) User.builder().username("jwt").password("api").authorities("ROLE_API").build();

        Authentication returnAuthentication = new PreAuthenticatedAuthenticationToken(user, token, user.getAuthorities());
        returnAuthentication.setAuthenticated(true);

        return returnAuthentication;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return (TokenAuthentication.class.isAssignableFrom(aClass));
    }
}
