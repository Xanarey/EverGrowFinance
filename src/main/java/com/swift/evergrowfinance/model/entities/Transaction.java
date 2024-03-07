package com.swift.evergrowfinance.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.swift.evergrowfinance.model.enums.Currency;
import com.swift.evergrowfinance.model.enums.TransactionStatus;
import com.swift.evergrowfinance.model.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transactions")
public class Transaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount")
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private Currency currency;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Column(name = "sender_phone_number")
    private String senderPhoneNumber;

    @Column(name = "recipient_phone_number")
    private String recipientPhoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TransactionStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TransactionType type;

    @Column(name = "description")
    private String description;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;
}
