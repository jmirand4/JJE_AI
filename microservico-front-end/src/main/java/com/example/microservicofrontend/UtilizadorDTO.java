package com.example.microservicofrontend;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Data
public class UtilizadorDTO implements UserDetails {

    @JsonProperty("userId")
    private Integer userId;

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("estaBloqueado")
    private Boolean estaBloqueado;

    @JsonProperty("role")
    @Transient
    private RoleDTO role;


    public UtilizadorDTO(String username, String password, RoleDTO role) {
        this.username = username;
        this.password = password;
        this.estaBloqueado= false;
        this.role = role;
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (getRole().getName().equalsIgnoreCase("ADMIN"))
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        else if (getRole().getName().equalsIgnoreCase("NORMAL"))
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_NORMAL"));
        else
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_PREMIUM"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !estaBloqueado;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

