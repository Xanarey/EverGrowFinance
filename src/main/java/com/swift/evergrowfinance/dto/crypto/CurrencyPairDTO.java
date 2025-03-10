package com.swift.evergrowfinance.dto.crypto;

import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyPairDTO {
    private String advNo;
    private double price;
    private double minAmount;
    private double maxAmount;
    private String asset;
    private String fiatUnit;
    private String advertiserNickName;
}


