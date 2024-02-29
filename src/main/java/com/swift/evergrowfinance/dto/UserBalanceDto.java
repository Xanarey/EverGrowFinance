package com.swift.evergrowfinance.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class UserBalanceDto {
    private String email;
    private String phoneNumber;
    private String walletType;
    private BigDecimal balance;
}
