package com.swift.evergrowfinance.dto;

import lombok.Data;

@Data
public class TwoFactorRequestDTO {

    private String email;
    private String code;

    public TwoFactorRequestDTO(String email, String code) {
        this.email = email;
        this.code = code;
    }

}
