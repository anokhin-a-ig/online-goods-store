package ru.anokhin.dev.onlinegoodsstore.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;
import java.util.UUID;

@Entity
@Data
public class Role implements GrantedAuthority {

    @Id
    private UUID uuid;

    private String roleName;

    @ManyToMany
    private Set<User> users;

    @Override
    public String getAuthority() {
        return getRoleName();
    }

}
