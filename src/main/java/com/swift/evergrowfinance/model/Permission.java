package com.swift.evergrowfinance.model;

public enum Permission {

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
