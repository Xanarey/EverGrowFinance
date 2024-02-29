package com.swift.evergrowfinance.dto;

import com.swift.evergrowfinance.model.enums.Currency;
import com.swift.evergrowfinance.model.enums.TransactionStatus;
import com.swift.evergrowfinance.model.enums.TransactionType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MoneyTransferRequest {
    private BigDecimal amount;
    private Currency currency;
    private String senderPhoneNumber;
    private String recipientPhoneNumber;
    private String description;
    private TransactionType type;

    public MoneyTransferRequest(BigDecimal amount, Currency currency, String senderPhoneNumber, String recipientPhoneNumber, String description, TransactionType type) {
        this.amount = amount;
        this.currency = currency;
        this.senderPhoneNumber = senderPhoneNumber;
        this.recipientPhoneNumber = recipientPhoneNumber;
        this.description = description;
        this.type = type;
    }
}
