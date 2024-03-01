package com.swift.evergrowfinance.dto;

import lombok.Data;

@Data
public class TransferResponseDTO {
    private String message;
    public TransferResponseDTO(String message) {
        this.message = message;
    }
}
