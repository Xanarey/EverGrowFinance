package com.swift.evergrowfinance.dto;

import lombok.Data;

@Data
public class AuthRequestDto {
    private String email;
    private String password;

    public AuthRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}