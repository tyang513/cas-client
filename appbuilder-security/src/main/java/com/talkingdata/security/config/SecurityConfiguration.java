package com.talkingdata.security.config;

import com.talkingdata.security.authentication.AppBuilderAuthenticationFilter;
import com.talkingdata.security.authorization.cas.AjaxAuthenticationFailureHandler;
import com.talkingdata.security.authorization.cas.AjaxAuthenticationSuccessHandler;
import com.talkingdata.security.authorization.cas.AjaxLogoutSuccessHandler;
import com.talkingdata.security.authorization.cas.CustomSessionFixationProtectionStrategy;
import com.talkingdata.security.authorization.cas.CustomSingleSignOutFilter;
import com.talkingdata.security.authorization.cas.RememberCasAuthenticationEntryPoint;
import com.talkingdata.security.authorization.cas.RememberCasAuthenticationProvider;
import com.talkingdata.security.authorization.cas.RememberWebAuthenticationDetailsSource;
import com.talkingdata.security.authorization.cas.UserDetailsServiceImpl;
import com.talkingdata.security.authorization.token.TokenAuthenticationFilter;
import com.talkingdata.security.authorization.token.TokenAuthenticationProvider;
import com.talkingdata.security.authorization.token.TokenExceptionTranslationFilter;
import com.talkingdata.security.authorization.token.TokenValidator;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.inject.Inject;

/**
 * Web安全配置中心
 *
 * @author ruobin.yang
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);

    /**
     * 安全配置项
     */
    @Autowired
    private SecurityConfig securityConfig;

    /**
     * ajax 成功处理
     */
    @Autowired
    private AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler;

    /**
     * ajax 失败处理
     */
    @Autowired
    private AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler;

    /**
     * ajax 登出处理
     */
    @Autowired
    private AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;

    /**
     * 安全忽略配置
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/webjars/springfox-swagger-ui/**/*.{js,html,css}")
                .antMatchers("/druid2/**/*.{js,html,css}")
                .antMatchers("/swagger-ui.html")
                .antMatchers("/index.html")
                .antMatchers("/css/**")
                .antMatchers("/html/**")
                .antMatchers("/images/**")
                .antMatchers("/js/**")
                .antMatchers("/i18n/**")
                .antMatchers("/meta/catalogs/cascade/rest")
                .antMatchers("/meta/metas/rest/**")
                .antMatchers("/meta/metas/rest")
                .antMatchers("/meta/versions/rest");
    }

    /**
     * 认证配置
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .addFilterBefore(createTokenAuthenticationFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new TokenExceptionTranslationFilter(), ExceptionTranslationFilter.class)
                .addFilterBefore(createCasAuthenticationFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(createSingleSignOutFilter(), CasAuthenticationFilter.class)
                .addFilterBefore(requestCasGlobalLogoutFilter(), LogoutFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(casAuthenticationEntryPoint())
                .and()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/**").authenticated()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(ajaxLogoutSuccessHandler)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll();
    }

    /**
     * 注册授权过滤器,用于认证通过后设置用户的授权信息
     *
     * @return FilterRegistrationBean
     */
    @Bean
    public FilterRegistrationBean registrationAuthenticationFilter() {

        String appCode = securityConfig.getAppCode() == null ? "" : securityConfig.getAppCode();
        String appTaken = securityConfig.getAppTaken() == null ? "" : securityConfig.getAppTaken();
        String menu = securityConfig.getMenu() == null ? "" : securityConfig.getMenu();
        String button = securityConfig.getButton() == null ? "" : securityConfig.getButton();
        String typeList = securityConfig.getTypeList() == null ? "" : securityConfig.getTypeList();

        logger.info("注册授权过滤器,用于认证通过后设置用户权限信息");
        logger.info("appCode = {}", appCode);
        logger.info("appTaken = {}", appTaken);
        logger.info("menu = {}", menu);
        logger.info("button = {}", button);
        logger.info("typeList = {}", typeList);

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new AppBuilderAuthenticationFilter());
        registration.addUrlPatterns("/*");
        registration.addInitParameter("appcode", appCode);
        registration.addInitParameter("apptaken", appTaken);
        registration.addInitParameter("menu", menu);
        registration.addInitParameter("button", button);
        registration.addInitParameter("typeList", typeList);
        registration.setName("appBuilderAuthenticationFilter(MenuFilter)");
        registration.setOrder(10);
        return registration;
    }

    /**
     * 注册CAS 认证过滤器
     *
     * @return CasAuthenticationFilter
     * @throws Exception 异常
     */
    @Bean
    public CasAuthenticationFilter createCasAuthenticationFilter() throws Exception {
        CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();
        casAuthenticationFilter.setAuthenticationManager(authenticationManager());
        casAuthenticationFilter.setAuthenticationDetailsSource(new RememberWebAuthenticationDetailsSource());
        casAuthenticationFilter.setSessionAuthenticationStrategy(createSessionStrategy());
        casAuthenticationFilter.setAuthenticationFailureHandler(ajaxAuthenticationFailureHandler);
        casAuthenticationFilter.setAuthenticationSuccessHandler(ajaxAuthenticationSuccessHandler);
        return casAuthenticationFilter;
    }

    /**
     * 单点登录过滤器
     *
     * @return CustomSingleSignOutFilter
     */
    @Bean
    public CustomSingleSignOutFilter createSingleSignOutFilter() {
        CustomSingleSignOutFilter singleSignOutFilter = new CustomSingleSignOutFilter();
        singleSignOutFilter.setCasServerUrlPrefix(securityConfig.getCasUrlPrefix());
        return singleSignOutFilter;
    }

    /**
     * 登出过滤器
     *
     * @return LogoutFilter
     */
    @Bean
    public LogoutFilter requestCasGlobalLogoutFilter() {
        LogoutFilter logoutFilter = new LogoutFilter(securityConfig.getCasServiceLogout() + "?service="
                + securityConfig.getAppServiceHome(), new SecurityContextLogoutHandler());
        logoutFilter.setLogoutRequestMatcher(new AntPathRequestMatcher("/api/logout", "POST"));
        return logoutFilter;
    }

    /**
     * 注册全局的认证中心
     *
     * @param auth 认证
     */
    @Inject
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(createTokenAuthenticationProvider());
        auth.authenticationProvider(createCasAuthenticationProvider());
    }

    /**
     * 创建Session Strategy
     * @return SessionAuthenticationStrategy
     */
    @Bean
    public SessionAuthenticationStrategy createSessionStrategy() {
        SessionFixationProtectionStrategy sessionStrategy = new CustomSessionFixationProtectionStrategy();
        sessionStrategy.setMigrateSessionAttributes(false);
        return sessionStrategy;
    }

    /**
     * 创建CAS认证端点
     *
     * @return RememberCasAuthenticationEntryPoint
     */
    @Bean
    public RememberCasAuthenticationEntryPoint casAuthenticationEntryPoint() {
        RememberCasAuthenticationEntryPoint casAuthenticationEntryPoint = new RememberCasAuthenticationEntryPoint();
        casAuthenticationEntryPoint.setLoginUrl(securityConfig.getCasServiceLogin());
        casAuthenticationEntryPoint.setServiceProperties(createServiceProperties());
        casAuthenticationEntryPoint.setPathLogin("/api/login");
        return casAuthenticationEntryPoint;
    }

    /**
     * 创建Spring Security ServiceProperties
     *
     * @return ServiceProperties
     */
    @Bean
    public ServiceProperties createServiceProperties() {
        ServiceProperties serviceProperties = new ServiceProperties();
        serviceProperties.setService(securityConfig.getAppServiceSecurity());
        serviceProperties.setSendRenew(false);
        return serviceProperties;
    }

    /**
     * CAS认证
     *
     * @return RememberCasAuthenticationProvider
     */
    @Bean
    protected RememberCasAuthenticationProvider createCasAuthenticationProvider() {
        RememberCasAuthenticationProvider casAuthenticationProvider = new RememberCasAuthenticationProvider();
        casAuthenticationProvider.setAuthenticationUserDetailsService(createUserDetailsService());
        casAuthenticationProvider.setServiceProperties(createServiceProperties());
        casAuthenticationProvider.setTicketValidator(createCas20ServiceTicketValidator());
        casAuthenticationProvider.setKey("an_id_for_this_auth_provider_only");
        return casAuthenticationProvider;
    }

    /**
     * 创建CAS 2.0协议ST认证
     *
     * @return Cas20ServiceTicketValidator
     */
    @Bean
    public Cas20ServiceTicketValidator createCas20ServiceTicketValidator() {
        return new Cas20ServiceTicketValidator(securityConfig.getCasUrlPrefix());
    }

    /**
     * 用户信息服务
     *
     * @return AuthenticationUserDetailsService<CasAssertionAuthenticationToken>
     */
    @Bean
    public AuthenticationUserDetailsService<CasAssertionAuthenticationToken> createUserDetailsService() {
        return new UserDetailsServiceImpl();
    }

    /**
     * 令牌认证过滤器
     * @return TokenAuthenticationFilter
     */
    @Bean
    protected TokenAuthenticationFilter createTokenAuthenticationFilter(){
        return new TokenAuthenticationFilter(createTokenValidator());
    }

    /**
     * 令牌认证器
     * @return TokenValidator
     */
    @Bean
    public TokenValidator createTokenValidator(){
        String casUrlPrefix = securityConfig.getCasUrlPrefix();
        logger.info("创建TokenValidator cas.url.prefix = ", casUrlPrefix);
        return new TokenValidator(casUrlPrefix);
    }

    /**
     * 令牌认证
     *
     * @return TokenAuthenticationProvider
     */
    @Bean
    protected TokenAuthenticationProvider createTokenAuthenticationProvider() {
        return new TokenAuthenticationProvider();
    }

}
