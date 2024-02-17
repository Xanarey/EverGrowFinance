package com.swift.evergrowfinance.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

@Data
public class UserDetailsDTO implements Serializable {
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsDTO() {
    }

    @JsonCreator
    public UserDetailsDTO(@JsonProperty("username") String username,
                          @JsonProperty("password") String password,
                          @JsonProperty("authorities") Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }
}
