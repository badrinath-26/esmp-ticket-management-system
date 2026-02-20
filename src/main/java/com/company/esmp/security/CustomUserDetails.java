package com.company.esmp.security;

import com.company.esmp.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    // ROLES → AUTHORITIES
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles()
                .stream()
                .map(role ->
                        new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toSet());
    }

    // PASSWORD FROM DB (BCrypt)
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // EMAIL ACTS AS USERNAME
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    // ACCOUNT FLAGS (WE KEEP ALL TRUE FOR NOW)
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
