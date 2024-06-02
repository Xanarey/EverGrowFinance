package com.swift.evergrowfinance.dto;

import lombok.Data;

@Data
public class ConfirmTransferRequestDTO {
    private String transferId;
    private String otp;
}
