package com.talkingdata.security.authorization.token;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * token验证
 * @author tao.yang
 * @date 2019-06-26
 */
public class TokenValidator {

    private static final Logger logger = LoggerFactory.getLogger(TokenValidator.class);

    /**
     * cas url prefix, exp : http://cas.example.com/sso
     */
    private final String casUrlPrefix;

    public TokenValidator(String casUrlPrefix) {
        this.casUrlPrefix = casUrlPrefix;
    }

    public void validate(String token) throws TokenValidationException{

        String url = casUrlPrefix + "/token?token=" + token;
        logger.info("验证token正确性 token : {}", token);

    }



}
