package com.talkingdata.security.authorization.cas;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author ruobin.yang
 */
public class AppUserDetails implements UserDetails {

    /** */
    private static final long serialVersionUID = -4777124807325532850L;

    private String userid;

    private Collection<? extends GrantedAuthority> authorities;

    private List<String> roles;

    public AppUserDetails() {
        super();
    }

    /**
     * @param userid
     * @param authorities
     */
    public AppUserDetails(String userid, Collection<? extends GrantedAuthority> authorities) {
        super();
        this.userid = userid;
        this.authorities = authorities;
        this.roles = new ArrayList<>(2);
        for (GrantedAuthority authority : authorities) {
            this.roles.add(authority.getAuthority());
        }
    }

    public String getUserid() {
        return userid;
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
        return userid;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UserDetails [userid=");
        sb.append(userid);
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

}
