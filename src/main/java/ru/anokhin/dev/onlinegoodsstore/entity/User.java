package ru.anokhin.dev.onlinegoodsstore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Entity (name = "User")
@Data
public class User implements UserDetails {

    @Id
    private UUID id;
    @Column
    private String username;
    @Column
    private String password;
    @OneToOne
    private Person person;
    @Column
    private Set<Role> roles;
    @Column
    private LocalDateTime createAt;
    @Column
    private boolean isBlocked;
    @Column
    private LocalDateTime blockedAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
