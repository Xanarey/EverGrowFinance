package com.swift.evergrowfinance.service;

import com.swift.evergrowfinance.model.Transaction;
import com.swift.evergrowfinance.model.WalletType;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {

    List<Transaction> getAllTransactions();
    void savingTransaction(Long fromAccountId, Long toAccountId, BigDecimal amount, WalletType walletType, String phoneNumber);
}
