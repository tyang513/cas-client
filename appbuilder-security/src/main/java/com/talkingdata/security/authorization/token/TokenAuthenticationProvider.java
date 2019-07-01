package com.talkingdata.security.authorization.token;

import com.talkingdata.security.authorization.cas.AppUserDetails;
import com.talkingdata.security.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Map;

/**
 * 令牌认证
 * @author tao.yang
 * @date 2019-06-26
 */
public class TokenAuthenticationProvider implements AuthenticationProvider{

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationProvider.class);

    /**
     * 令牌验证
     */
    private TokenValidator tokenValidator;

    public TokenAuthenticationProvider(TokenValidator tokenValidator) {
        this.tokenValidator = tokenValidator;
    }

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
        Map<String, Object> tokenUserMap = null;
        try{
            String validationString = tokenValidator.validate(token);
            logger.info("validation {}" , validationString);
            tokenUserMap = CommonUtils.readValue2Map(validationString);
        }
        catch (Exception e){
            throw new TokenValidationException("Token 认证或解析异常", e);
        }
        ;
        // 这里验证token
        String userName = (String) tokenUserMap.get("userName");
        String tenantId = (String) tokenUserMap.get("tenantId");
        String email = (String) tokenUserMap.get("email");
        String loginId = (String) tokenUserMap.get("loginId");
        String mobile = (String) tokenUserMap.get("mobile");
        String status = (String) tokenUserMap.get("status");
        String issuer = (String) tokenUserMap.get("issuer");
        String audience = (String) tokenUserMap.get("audience");
        String jwtID = (String) tokenUserMap.get("jwtID");
        String issueTime = (String) tokenUserMap.get("issueTime");
        String subject = (String) tokenUserMap.get("subject");

        UserDetails user = AppUserDetails.builder().username(userName).tenantId(tenantId).email(email)
                .loginId(loginId).mobile(mobile).status(status).issuer(issuer).audience(audience).jwtID(jwtID)
                .issueTime(issueTime).subject(subject).authorities("ROLE_API").build();

        Authentication returnAuthentication = new PreAuthenticatedAuthenticationToken(user, token, user.getAuthorities());
        returnAuthentication.setAuthenticated(true);

        return returnAuthentication;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return (TokenAuthentication.class.isAssignableFrom(aClass));
    }
}
