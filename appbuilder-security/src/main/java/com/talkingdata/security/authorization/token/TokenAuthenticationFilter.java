package com.talkingdata.security.authorization.token;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 令牌认证过滤器
 *
 * @author tao.yang
 * @date 2019-06-26
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    /**
     * token验证器
     */
    private TokenValidator tokenValidator;

    /**
     * 默认的构造函数
     *
     * @param tokenValidator
     */
    public TokenAuthenticationFilter(TokenValidator tokenValidator) {
        this.tokenValidator = tokenValidator;
    }

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        SecurityContext context = SecurityContextHolder.getContext();
        logger.debug(" TokenAuthenticationFilter ");
        if (context.getAuthentication() != null && context.getAuthentication().isAuthenticated()) {
            // do nothing
        } else {
            String token = safeGetToken(request, TokenAuthentication.TOKEN);
            if (token != null && !"".equals(token)) {
                logger.info("进行token认证,获取token {}", token);

                Authentication auth = new TokenAuthentication(null, token);
                SecurityContextHolder.getContext().setAuthentication(auth);
                request.setAttribute("com.talkingdata.security.authorization.token.TokenAuthenticationFilter.FILTERED", true);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String safeGetToken(final HttpServletRequest request, final String parameter) {
        if (request.getHeader(parameter) != null || "".equals(request.getHeader(parameter))) {
            return request.getHeader(parameter);
        }
        String post = "POST";
        if (post.equals(request.getMethod())) {
            return request.getParameter(parameter);
        }
        return request.getQueryString() == null || !request.getQueryString().contains(parameter) ? null : request.getParameter(parameter);
    }

}
