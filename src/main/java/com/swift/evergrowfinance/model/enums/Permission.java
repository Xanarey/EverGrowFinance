package com.swift.evergrowfinance.model.enums;

import lombok.Getter;
import java.io.Serializable;

@Getter
public enum Permission implements Serializable {

    DEV_ADMIN("ADMIN"),
    DEV_USER("USER");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

}
