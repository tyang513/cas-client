package com.talkingdata.security.authorization.token;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author tao.yang
 * @date 2019-06-26
 */
public class TokenAuthentication extends AbstractAuthenticationToken {

    /**
     * 日志
     */
    private Logger logger = LoggerFactory.getLogger(TokenAuthentication.class);

    /**
     * 名称
     */
    public static final String TOKEN = "token";

    /**
     * token 令牌
     */
    private String token;

    /**
     * 默认的构造函数
     * @param authorities
     * @param token
     */
    public TokenAuthentication(Collection<? extends GrantedAuthority> authorities, String token) {
        super(authorities);
        this.token = token;
    }


    @Override
    public Object getCredentials() {
        return this.token;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
