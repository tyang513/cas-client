package com.talkingdata.security.authorization.cas;

import org.codehaus.jackson.map.ObjectMapper;
import org.jasig.cas.client.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.Assert;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ruobin.yang
 * Returns a 401 error code (Unauthorized) to the client when not on login url and if not
 * authenticated.
 */
public class RememberCasAuthenticationEntryPoint implements AuthenticationEntryPoint, InitializingBean {

    private final Logger log = LoggerFactory.getLogger(RememberCasAuthenticationEntryPoint.class);

    private ServiceProperties serviceProperties;

    private String casLoginUrl;

    private String pathLogin;

    private String targetUrlParameter = "spring-security-redirect";

    private boolean debug = true;
    /**
     * Determines whether the Service URL should include the session id for the specific user. As of
     * CAS 3.0.5, the session id will automatically be stripped. However, older versions of CAS
     * (i.e. CAS 2), do not automatically strip the session identifier (this is a bug on the part of
     * the older server implementations), so an option to disable the session encoding is provided
     * for backwards compatibility.
     * <p>
     * By default, encoding is enabled.
     *
     * @deprecated since 3.0.0 because CAS is currently on 3.3.5.
     */
    @Deprecated
    private boolean encodeServiceUrlWithSessionId = true;

    private boolean encodeServiceUrlWithSessionIdReplace=true;


    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.hasLength(this.casLoginUrl, "loginUrl must be specified");
        Assert.hasLength(this.pathLogin, "pathLogin must be specified");
        Assert.hasLength(this.targetUrlParameter, "targetUrlParameter must be specified");
        Assert.notNull(this.serviceProperties, "createServiceProperties must be specified");
        Assert.notNull(this.serviceProperties.getService(), "createServiceProperties.getService() cannot be null.");
    }

    @Override
    public final void commence(final HttpServletRequest request, final HttpServletResponse response,
                               final AuthenticationException authenticationException) throws IOException {

        HttpServletRequest httpRequest = request;


        String resourcePath = new UrlPathHelper().getPathWithinApplication(httpRequest);
        if (debug) {
            log.debug("=====================================================================> RESOURCEPATH {}",
                    resourcePath);
        }
        boolean isPostMessage = false;
        if (request.getQueryString() != null) {
            isPostMessage = request.getQueryString().contains("postMessage");
        }
        ;
        if (debug) {
            log.debug("=====================================================================> postMessage {}", isPostMessage);
        }
        // if (doCASAuth != null) {
        if (pathLogin.equals(resourcePath)) {

            final String urlEncodedService = createServiceUrl(request, response);
            final String redirectUrl = createRedirectUrl(urlEncodedService);
            if (debug) {
                log.debug("Pre-authenticated entry point called. Calling CAS Authentication with redirectURL {}",
                        redirectUrl);
            }
            preCommence(request, response);

            response.sendRedirect(redirectUrl);
        } else {
            if (debug) {
                log.debug("Pre-authenticated entry point called. Rejecting access");
            }
            PrintWriter wirte = response.getWriter();
            Map<String, String> result = new HashMap<>(16);
            String context = request.getContextPath();
            result.put("service", this.casLoginUrl + "?service=");
            result.put("redirect", context + "/login/cas?spring-security-redirect=");
            ObjectMapper mapper = new ObjectMapper();
            String results = mapper.writeValueAsString(result);
            wirte.print(results);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Denied");
        }

    }

    /**
     * Constructs a new Service Url. The default implementation relies on the CAS client to do the
     * bulk of the work.
     *
     * @param request  the HttpServletRequest
     * @param response the HttpServlet Response
     * @return the constructed service url. CANNOT be NULL.
     */
    protected String createServiceUrl(final HttpServletRequest request, final HttpServletResponse response) {
        String service = this.serviceProperties.getService();

        String uri = request.getRequestURI() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");

        String servletPath = request.getServletPath();
        if (uri != null && !uri.isEmpty()) {
            service += String.format("?%s=%s", this.targetUrlParameter, uri);
        }
        if (debug) {
            log.debug("serviceURL = {}", service);
        }
        return CommonUtils.constructServiceUrl(null, response, service, null,
                this.serviceProperties.getArtifactParameter(), this.encodeServiceUrlWithSessionIdReplace);
    }

    /**
     * Constructs the Url for Redirection to the CAS server. Default implementation relies on the
     * CAS client to do the bulk of the work.
     *
     * @param serviceUrl the service url that should be included.
     * @return the redirect url. CANNOT be NULL.
     */
    protected String createRedirectUrl(final String serviceUrl) {
        return CommonUtils.constructRedirectUrl(this.casLoginUrl, this.serviceProperties.getServiceParameter(),
                serviceUrl, this.serviceProperties.isSendRenew(), false);
    }

    /**
     * Template method for you to do your own pre-processing before the redirect occurs.
     *
     * @param request  the HttpServletRequest
     * @param response the HttpServletResponse
     */
    protected void preCommence(final HttpServletRequest request, final HttpServletResponse response) {

    }

    /**
     * The enterprise-wide CAS login URL. Usually something like
     * <code>https://www.mycompany.com/cas/login</code>.
     *
     * @return the enterprise-wide CAS login URL
     */
    public final String getLoginUrl() {
        return this.casLoginUrl;
    }

    public final ServiceProperties getServiceProperties() {
        return this.serviceProperties;
    }

    public final void setLoginUrl(final String loginUrl) {
        this.casLoginUrl = loginUrl;
    }

    public final void setServiceProperties(final ServiceProperties serviceProperties) {
        this.serviceProperties = serviceProperties;
    }

    public void setPathLogin(String pathLogin) {
        this.pathLogin = pathLogin;
    }

}
