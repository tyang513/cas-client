package com.talkingdata.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 属性配置加载
 *
 * @author tao.yang
 * @date 2019-03-19
 */
public class SecurityContextInitializer
        implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {

    /**
     * 日志
     */
    protected final Logger logger = LoggerFactory.getLogger(SecurityContextInitializer.class);

    /**
     * 默认顺序
     */
    public static final int DEFAULT_ORDER = 0;

    /**
     * sso地址
     */
    public static final String CAS_SREVICE_LOGIN = "cas.service.login";

    public static final String CAS_SREVICE_LOGOUT = "cas.service.logout";

    public static final String CAS_URL_PREFIX = "cas.url.prefix";

    public static final String UM_RMI_URL = "app.service.security";

    public static final String APP_SERVICE_HOME = "app.service.home";

    /**
     * Initialize the given application context.
     *
     * @param context the application to configure
     */
    @Override
    public void initialize(ConfigurableApplicationContext context) {
        logger.info("初始化sso配置项并进行校验");
        ConfigurableEnvironment environment = context.getEnvironment();

        String ssoService = environment.getProperty(CAS_SREVICE_LOGIN);
        if (ssoService == null || ssoService.equals("")) {
            logger.warn("cas.service.login未配置,无法集成sso");
            throw new RuntimeException("cas.service.login未配置,无法集成sso");
        }
        fillSystemPropertyFromEnvironment(environment, CAS_SREVICE_LOGIN);
        ssoService = environment.getProperty(CAS_SREVICE_LOGOUT);
        if (ssoService == null || ssoService.equals("")) {
            logger.warn("cas.service.logout未配置,无法集成sso");
            throw new RuntimeException("cas.service.logout未配置,无法集成sso");
        }
        fillSystemPropertyFromEnvironment(environment, CAS_SREVICE_LOGOUT);
        ssoService = environment.getProperty(CAS_URL_PREFIX);
        if (ssoService == null || ssoService.equals("")) {
            logger.warn("cas.url.prefix未配置,无法集成sso");
            throw new RuntimeException("cas.url.prefix未配置,无法集成sso");
        }
        fillSystemPropertyFromEnvironment(environment, CAS_URL_PREFIX);
        ssoService = environment.getProperty(UM_RMI_URL);
        if (ssoService == null || ssoService.equals("")) {
            logger.warn("app.service.security未配置,无法集成sso");
            throw new RuntimeException("app.service.security未配置,无法集成sso");
        }
        fillSystemPropertyFromEnvironment(environment, UM_RMI_URL);
        ssoService = environment.getProperty(APP_SERVICE_HOME);
        if (ssoService == null || ssoService.equals("")) {
            logger.warn("app.service.home未配置,无法集成sso");
            throw new RuntimeException("app.service.home未配置,无法集成sso");
        }
        fillSystemPropertyFromEnvironment(environment, APP_SERVICE_HOME);
        logger.info("sso地址配置为：cas.url.prefix " + ssoService);
    }

    private void fillSystemPropertyFromEnvironment(ConfigurableEnvironment environment, String propertyName) {
        if (System.getProperty(propertyName) != null) {
            return;
        }
        String propertyValue = environment.getProperty(propertyName);

        if (propertyValue == null || "".equals(propertyValue)) {
            return;
        }
        System.setProperty(propertyName, propertyValue);
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }
}
