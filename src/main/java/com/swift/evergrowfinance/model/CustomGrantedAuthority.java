package com.swift.evergrowfinance.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
public class CustomGrantedAuthority implements GrantedAuthority {

    private final String authority;

    @JsonCreator
    public CustomGrantedAuthority(@JsonProperty("authority") String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

}