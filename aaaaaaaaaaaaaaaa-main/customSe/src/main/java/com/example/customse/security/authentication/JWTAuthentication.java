package com.example.customse.security.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

public class JWTAuthentication implements Authentication {
    private final Object principal;                        //like UserDetails
    private final Object credentials;                     //like Jwttoken


//    private static final long serialVersionUID = 1L; //this is good approach
//    private final Serializable principal;                  // user identity
//    private final transient Serializable credentials;      // JWT token (not serialized)

    private final Collection<? extends GrantedAuthority> authorities; //roles
    private boolean authenticated;                                     //after authentication true or false

    public JWTAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, boolean authenticated) {
        this.principal = principal;
        this.credentials = credentials;
        this.authorities = authorities;
        this.authenticated = authenticated;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

//    @Override
//    public String getName() {
//        if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
//            return ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
//        }
//        return principal != null ? principal.toString() : "";
//    }
@Override
public String getName() {
    if (principal instanceof UserDetails userDetails) {
        return userDetails.getUsername();
    }
    return principal != null ? principal.toString() : "";
}
}
