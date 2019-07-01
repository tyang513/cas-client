package com.talkingdata.security.authorization.cas;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author ruobin.yang
 */
public class AppUserDetails implements UserDetails {

    /** */
    private static final long serialVersionUID = -4777124807325532850L;

    private String userName;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 邮箱
     */
    private String email;

    private String loginId;

    private String mobile;

    private String status;

    private String issuer;

    private String audience;

    private String jwtID;

    private String issueTime;

    private String subject;

    private Collection<? extends GrantedAuthority> authorities;

    private List<String> roles;

    public AppUserDetails() {
        super();
    }

    /**
     * @param userName
     * @param authorities
     */
    public AppUserDetails(String userName, Collection<? extends GrantedAuthority> authorities) {
        super();
        this.userName = userName;
        this.authorities = authorities;
        this.roles = new ArrayList<>(2);
        for (GrantedAuthority authority : authorities) {
            this.roles.add(authority.getAuthority());
        }
    }

    public AppUserDetails(String userName, String tenantId, String email, String loginId, String mobile, String status,
                          String issuer, String audience, String jwtID, String issueTime, String subject,
                          Collection<? extends GrantedAuthority> authorities) {
        super();
        this.userName = userName;
        this.tenantId = tenantId;
        this.email = email;
        this.loginId = loginId;
        this.mobile = mobile;
        this.status = status;
        this.issuer = issuer;
        this.audience = audience;
        this.jwtID = jwtID;
        this.issueTime = issueTime;
        this.subject = subject;
        this.authorities = authorities;
        for (GrantedAuthority authority : authorities) {
            this.roles.add(authority.getAuthority());
        }
    }

    public String getUserid() {
        return userName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static AppUserDetails.AppUserBuilder builder() {
        return new AppUserDetails.AppUserBuilder();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UserDetails [userid=");
        sb.append(userName);
        sb.append(", authorities=");
        sb.append(roles.toArray());
        sb.append(", isAccountNonExpired()=");
        sb.append(isAccountNonExpired());
        sb.append(", isAccountNonLocked()=");
        sb.append(isAccountNonLocked());
        sb.append(", isCredentialsNonExpired()=");
        sb.append(isCredentialsNonExpired());
        sb.append(", isEnabled()=");
        sb.append(isEnabled());
        sb.append(']');
        return sb.toString();
    }

    public static class AppUserBuilder {
        private String username;
        private String tenantId;
        private String email;
        private String loginId;
        private String mobile;
        private String status;
        private String issuer;
        private String audience;
        private String jwtID;
        private String issueTime;
        private String subject;
        private List<GrantedAuthority> authorities;

        /**
         * Creates a new instance
         */
        private AppUserBuilder() {
        }

        public AppUserDetails.AppUserBuilder username(String username) {
            Assert.notNull(username, "username cannot be null");
            this.username = username;
            return this;
        }


        public AppUserDetails.AppUserBuilder password(String password) {
            Assert.notNull(password, "password cannot be null");
            return this;
        }

        public AppUserDetails.AppUserBuilder tenantId(String tenantId) {
            Assert.notNull(tenantId, "tenantId cannot be null");
            this.tenantId = tenantId;
            return this;
        }


        public AppUserDetails.AppUserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public AppUserDetails.AppUserBuilder loginId(String loginId) {
            this.loginId = loginId;
            return this;
        }

        public AppUserDetails.AppUserBuilder mobile(String mobile) {
            this.mobile = mobile;
            return this;
        }

        public AppUserDetails.AppUserBuilder status(String status) {
            this.status = status;
            return this;
        }

        public AppUserDetails.AppUserBuilder issuer(String issuer) {
            this.issuer = issuer;
            return this;
        }

        public AppUserDetails.AppUserBuilder audience(String audience) {
            this.audience = audience;
            return this;
        }

        public AppUserDetails.AppUserBuilder jwtID(String jwtID) {
            this.jwtID = jwtID;
            return this;
        }

        public AppUserDetails.AppUserBuilder issueTime(String issueTime) {
            this.issueTime = issueTime;
            return this;
        }

        public AppUserDetails.AppUserBuilder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public AppUserDetails.AppUserBuilder authorities(GrantedAuthority... authorities) {
            return authorities(Arrays.asList(authorities));
        }


        public AppUserDetails.AppUserBuilder authorities(Collection<? extends GrantedAuthority> authorities) {
            this.authorities = new ArrayList<>(authorities);
            return this;
        }


        public AppUserDetails.AppUserBuilder authorities(String... authorities) {
            return authorities(AuthorityUtils.createAuthorityList(authorities));
        }

        public UserDetails build() {
            return new AppUserDetails(username, tenantId, email, loginId, mobile, status, issuer, audience, jwtID, issueTime, subject, authorities);
        }
    }

}


