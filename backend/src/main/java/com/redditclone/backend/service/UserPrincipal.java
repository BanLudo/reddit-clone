package com.redditclone.backend.service;

import com.redditclone.backend.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;


public class UserPrincipal implements UserDetails {

    private final long userId;
    private final String email;
    private final String password;
    private final String username;

    public static UserPrincipal create(Optional<User> user) {

        return new UserPrincipal(user.get().getUserId(),
                                 user.get().getEmail(),
                                 user.get().getPassword(),
                                 user.get().getUsername());
    }

    private UserPrincipal(long userId, String email, String password, String username){
        this.userId = Objects.requireNonNull(userId, "Id cannot be null");
        this.email = Objects.requireNonNull(email, "Email cannot be null");
        this.password = Objects.requireNonNull(password, "Password cannot be null");
        this.username = Objects.requireNonNull(username, "Username cannot be null");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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

    public long getUserId() {
        return userId;
    }

    public String getEmail(){
        return email;
    }
}
