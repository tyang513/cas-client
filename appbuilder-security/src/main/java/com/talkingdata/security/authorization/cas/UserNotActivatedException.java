package com.talkingdata.security.authorization.cas;

import org.springframework.security.core.AuthenticationException;

/**
 * @author ruobin.yang
 * This exception is throw in case of a not activated user trying to authenticate.
 */
public class UserNotActivatedException extends AuthenticationException {

    public UserNotActivatedException(String message) {
        super(message);
    }

    public UserNotActivatedException(String message, Throwable t) {
        super(message, t);
    }
}
