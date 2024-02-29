package com.swift.evergrowfinance.service;

import com.swift.evergrowfinance.dto.MoneyTransferRequest;
import com.swift.evergrowfinance.model.entities.Transaction;

import java.util.List;

public interface TransactionService {

    List<Transaction> getTransactionsForUser(Long id);
    List<Transaction> getAllTransactions();
    void savingTransaction(MoneyTransferRequest request);
}
