package com.darshan.auth.security;

import com.darshan.auth.entity.User;
import com.darshan.auth.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    public Long getId() { return user.getId(); }
    public String getEmail() { return user.getEmail(); }
    public Role getRole() { return user.getRole(); }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Map enum Role.USER -> ROLE_USER, Role.ADMIN -> ROLE_ADMIN
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    @Override public String getPassword() { return user.getPassword(); }
    @Override public String getUsername() { return user.getUsername(); }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return user.isEnabled(); }
}