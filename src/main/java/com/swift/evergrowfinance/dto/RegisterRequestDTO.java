package com.swift.evergrowfinance.dto;

import com.swift.evergrowfinance.model.enums.WalletType;
import lombok.Data;

@Data
public class RegisterRequestDTO {
    private String email;
    private String password;
    private String phoneNumber;
    private WalletType walletType;
}
