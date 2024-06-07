package com.swift.evergrowfinance.dto;

import lombok.Data;

@Data
public class PasswordResetDTO {
    private String token;
    private String password;
}
