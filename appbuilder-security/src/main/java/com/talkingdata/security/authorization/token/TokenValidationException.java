package com.talkingdata.security.authorization.token;

import org.springframework.security.core.AuthenticationException;

/**
 * @author tao.yang
 * @date 2019-06-26
 */
public class TokenValidationException extends AuthenticationException {

    /**
     * Unique Id for Serialization
     */
    private static final long serialVersionUID = 2126574356383320488L;

    /**
     * Constructs an exception with the supplied message.
     *
     * @param string the message
     */
    public TokenValidationException(final String string) {
        super(string);
    }

    /**
     * Constructs an exception with the supplied message and chained throwable.
     *
     * @param string    the message
     * @param throwable the original exception
     */
    public TokenValidationException(final String string, final Throwable throwable) {
        super(string, throwable);
    }
}
