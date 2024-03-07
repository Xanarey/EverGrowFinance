package com.swift.evergrowfinance.dto;

import com.swift.evergrowfinance.model.entities.Transaction;
import com.swift.evergrowfinance.model.enums.Currency;
import com.swift.evergrowfinance.model.enums.TransactionStatus;
import com.swift.evergrowfinance.model.enums.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionDTO implements Serializable {

    private BigDecimal amount;
    private Currency currency;
    private LocalDateTime dateTime;
    private String senderPhoneNumber;
    private String recipientPhoneNumber;
    private TransactionStatus status;
    private TransactionType type;
    private String description;

    public static TransactionDTO transactionDTO(Transaction transaction) {
        return TransactionDTO.builder()
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .dateTime(transaction.getDateTime())
                .senderPhoneNumber(transaction.getSenderPhoneNumber())
                .recipientPhoneNumber(transaction.getRecipientPhoneNumber())
                .status(transaction.getStatus())
                .type(transaction.getType())
                .description(transaction.getDescription())
                .build();
    }
}
