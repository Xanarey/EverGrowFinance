package com.swift.evergrowfinance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferResponseDTO {
    private String message;
    private boolean requiresOtp;
    private String transferId;

    public TransferResponseDTO(String message) {
        this.message = message;
    }
}
