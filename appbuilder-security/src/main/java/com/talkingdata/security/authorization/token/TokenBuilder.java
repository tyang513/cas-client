package com.talkingdata.security.authorization.token;

import org.jasig.cas.client.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author tao.yang
 * @date 2019-06-28
 */
public class TokenBuilder {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(TokenBuilder.class);

    /**
     * cas.url.prefix参数
     */
    private final String casUrlPrefix;

    /**
     * app.service.home参数
     */
    private final String appServiceHome;

    public TokenBuilder(String casUrlPrefix, String appServiceHome) {
        this.casUrlPrefix = casUrlPrefix;
        this.appServiceHome = appServiceHome;
    }

    /**
     *
     * @param serviceTicket
     * @param userName
     * @return
     */
    public String build(String serviceTicket, String userName) {
        String url = constructUrl(serviceTicket, userName);
        logger.info("ST[{}] userName[{}]获取token ,url[{}]", serviceTicket, userName, url);
        String token = CommonUtils.getResponseFromServer(url, "UTF-8");
        return token;
    }

    /**
     *
     * @param serviceTicket
     * @param userName
     * @return
     */
    private String constructUrl(String serviceTicket, String userName){
        try {
            return casUrlPrefix + (casUrlPrefix.endsWith("/") ? "" : "/") + "token/" + serviceTicket + "?service=" + URLEncoder.encode(appServiceHome, "UTF-8") + "&userName=" + userName;
        }
        catch (final UnsupportedEncodingException e){
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        TokenBuilder builder = new TokenBuilder("http://172.30.112.192:9097/sso", "http://172.30.112.192:8083/cas-client");
        builder.build("ST-1-k8yGG8aqnP7JpN-6NR4n2z32J2Qyangtao", "um_super_admin");
    }

}
