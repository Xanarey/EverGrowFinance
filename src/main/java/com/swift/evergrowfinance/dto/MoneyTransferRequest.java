package com.swift.evergrowfinance.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MoneyTransferRequest {
    private String fromPhoneNumber;
    private String toPhoneNumber;
    private BigDecimal amount;
}
