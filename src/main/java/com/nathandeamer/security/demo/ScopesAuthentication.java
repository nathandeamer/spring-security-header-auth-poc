package com.nathandeamer.security.demo;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

public class ScopesAuthentication implements Authentication {

    private final String principal;
    private final ArrayList<? extends GrantedAuthority> authorities;

    public ScopesAuthentication(String principal, ArrayList<? extends GrantedAuthority> authorities) {
        this.principal = principal;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public String getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        // no-op
    }

    @Override
    public String getName() {
        return null;
    }
}
