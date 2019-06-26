package com.talkingdata.security.authorization.cas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ruobin.yang
 * Authenticate a user from the database.
 */
public class CustomUserDetailsServiceImpl implements AuthenticationUserDetailsService<CasAssertionAuthenticationToken> {

    private final Logger log = LoggerFactory.getLogger(CustomUserDetailsServiceImpl.class);
    private boolean debug = true;

    public CustomUserDetailsServiceImpl() {
        super();
    }

    @Override
    public UserDetails loadUserDetails(CasAssertionAuthenticationToken token) throws UsernameNotFoundException {
        String login = token.getPrincipal().toString();
        String lowercaseLogin = login.toLowerCase();
        if (debug) {
            log.debug("Authenticating '{}'", login);
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();

        grantedAuthorities.add(new GrantedAuthority() {
            private static final long serialVersionUID = 1L;

            @Override
            public String getAuthority() {
                return AuthoritiesConstants.USER;
            }
        });

        return new AppUserDetails(lowercaseLogin, grantedAuthorities);
    }
}
