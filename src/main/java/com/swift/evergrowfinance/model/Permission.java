package com.swift.evergrowfinance.model;

import java.io.Serializable;

public enum Permission implements Serializable {

    DEV_ADMIN("ADMIN"),
    DEV_USER("USER");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

}
