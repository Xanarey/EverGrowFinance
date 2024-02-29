package com.swift.evergrowfinance.model.enums;

import com.swift.evergrowfinance.model.entities.CustomGrantedAuthority;
import lombok.Getter;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum Role implements Serializable {

    ADMIN(Set.of(Permission.DEV_ADMIN)),
    USER(Set.of(Permission.DEV_USER));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<CustomGrantedAuthority> getAuthorities() {
        return getPermissions().stream()
                .map(permission -> new CustomGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }

}
