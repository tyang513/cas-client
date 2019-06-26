package com.talkingdata.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author tao.yang
 * @date 2019-06-26
 */
@Configuration
public class SecurityConfig {

    /**
     * cas.service.login
     */
    @Value("${cas.url.prefix}")
    private String casUrlPrefix;

    /**
     * cas.service.login
     */
    @Value("${cas.service.login}")
    private String casServiceLogin;

    /**
     * cas.service.logout
     */
    @Value("${cas.service.logout}")
    private String casServiceLogout;

    /**
     * app.service.security
     */
    @Value("${app.service.security}")
    private String appServiceSecurity;

    /**
     * app.service.home
     */
    @Value("${app.service.home}")
    private String appServiceHome;

    private String appCode;

    private String appTaken;

    private String menu;

    private String button;

    private String typeList;

    public String getCasUrlPrefix() {
        return casUrlPrefix;
    }

    public void setCasUrlPrefix(String casUrlPrefix) {
        this.casUrlPrefix = casUrlPrefix;
    }

    public String getCasServiceLogin() {
        return casServiceLogin;
    }

    public void setCasServiceLogin(String casServiceLogin) {
        this.casServiceLogin = casServiceLogin;
    }

    public String getCasServiceLogout() {
        return casServiceLogout;
    }

    public void setCasServiceLogout(String casServiceLogout) {
        this.casServiceLogout = casServiceLogout;
    }

    public String getAppServiceSecurity() {
        return appServiceSecurity;
    }

    public void setAppServiceSecurity(String appServiceSecurity) {
        this.appServiceSecurity = appServiceSecurity;
    }

    public String getAppServiceHome() {
        return appServiceHome;
    }

    public void setAppServiceHome(String appServiceHome) {
        this.appServiceHome = appServiceHome;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getAppTaken() {
        return appTaken;
    }

    public void setAppTaken(String appTaken) {
        this.appTaken = appTaken;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getButton() {
        return button;
    }

    public void setButton(String button) {
        this.button = button;
    }

    public String getTypeList() {
        return typeList;
    }

    public void setTypeList(String typeList) {
        this.typeList = typeList;
    }
}
