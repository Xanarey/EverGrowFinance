package com.swift.evergrowfinance.dto;

import com.swift.evergrowfinance.model.entities.Wallet;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class UserBalanceDTO {
    private String email;
    private String phoneNumber;
    private String walletType;
    private BigDecimal balance;

    public static UserBalanceDTO fromWallet(Wallet wallet) {
        return UserBalanceDTO.builder()
                .email(wallet.getUser().getEmail())
                .phoneNumber(wallet.getPhoneNumber())
                .walletType(wallet.getWalletType().name())
                .balance(wallet.getBalance())
                .build();
    }
}
