package com.talkingdata.security.authorization.cas;

import org.jasig.cas.client.session.SessionMappingStorage;
import org.jasig.cas.client.util.AbstractConfigurationFilter;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Created by jgribonvald on 01/06/16.
 */

/**
 * Implements the Single Sign Out protocol.  It handles registering the session and destroying the session.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.1
 */
public final class CustomSingleSignOutFilter extends AbstractConfigurationFilter {

    public final static String DEFAULT_ARTIFACT_PARAMETER_NAME = "ticket";
    public final static String DEFAULT_LOGOUT_PARAMETER_NAME = "logoutRequest";
    public final static String DEFAULT_FRONT_LOGOUT_PARAMETER_NAME = "SAMLRequest";
    public final static String DEFAULT_RELAY_STATE_PARAMETER_NAME = "RelayState";
    private static final CustomSingleSignOutHandler HANDLER = new CustomSingleSignOutHandler();

    private AtomicBoolean handlerInitialized = new AtomicBoolean(false);

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        if (!isIgnoreInitConfiguration()) {
            HANDLER.setArtifactParameterName(getPropertyFromInitParams(filterConfig, "artifactParameterName",
                    CustomSingleSignOutFilter.DEFAULT_ARTIFACT_PARAMETER_NAME));
            HANDLER.setLogoutParameterName(getPropertyFromInitParams(filterConfig, "logoutParameterName",
                    CustomSingleSignOutFilter.DEFAULT_LOGOUT_PARAMETER_NAME));
            HANDLER.setFrontLogoutParameterName(getPropertyFromInitParams(filterConfig, "frontLogoutParameterName",
                    CustomSingleSignOutFilter.DEFAULT_FRONT_LOGOUT_PARAMETER_NAME));
            HANDLER.setRelayStateParameterName(getPropertyFromInitParams(filterConfig, "relayStateParameterName",
                    CustomSingleSignOutFilter.DEFAULT_RELAY_STATE_PARAMETER_NAME));
            HANDLER.setCasServerUrlPrefix(getPropertyFromInitParams(filterConfig, "casServerUrlPrefix", ""));
            HANDLER.setArtifactParameterOverPost(parseBoolean(getPropertyFromInitParams(filterConfig,
                    "artifactParameterOverPost", "false")));
            HANDLER.setEagerlyCreateSessions(parseBoolean(getPropertyFromInitParams(filterConfig,
                    "eagerlyCreateSessions", "true")));
        }
        HANDLER.init();
        handlerInitialized.set(true);
    }

    public void setArtifactParameterName(final String name) {
        HANDLER.setArtifactParameterName(name);
    }

    public void setLogoutParameterName(final String name) {
        HANDLER.setLogoutParameterName(name);
    }

    public void setFrontLogoutParameterName(final String name) {
        HANDLER.setFrontLogoutParameterName(name);
    }

    public void setRelayStateParameterName(final String name) {
        HANDLER.setRelayStateParameterName(name);
    }

    public void setCasServerUrlPrefix(final String casServerUrlPrefix) {
        HANDLER.setCasServerUrlPrefix(casServerUrlPrefix);
    }

    public void setSessionMappingStorage(final SessionMappingStorage storage) {
        HANDLER.setSessionMappingStorage(storage);
    }

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
                         final FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        /**
         * <p>Workaround for now for the fact that Spring Security will fail since it doesn't call {@link #init(FilterConfig)}.</p>
         * <p>Ultimately we need to allow deployers to actually inject their fully-initialized {@link SingleSignOutHandler}.</p>
         */
        if (!this.handlerInitialized.getAndSet(true)) {
            HANDLER.init();
        }

        if (HANDLER.isTokenRequest(request)) {
            filterChain.doFilter(servletRequest, servletResponse);
            HANDLER.process(request, response);
        } else if (HANDLER.process(request, response)) {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {
    }

    protected static CustomSingleSignOutHandler getSingleSignOutHandler() {
        return HANDLER;
    }
}

