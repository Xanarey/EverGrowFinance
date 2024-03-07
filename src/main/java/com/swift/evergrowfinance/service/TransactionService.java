package com.swift.evergrowfinance.service;

import com.swift.evergrowfinance.dto.MoneyTransferRequestDTO;
import com.swift.evergrowfinance.model.entities.Transaction;
import com.swift.evergrowfinance.model.entities.User;

import java.util.List;

public interface TransactionService {

    List<Transaction> getTransactionsForUser(Long id);
    List<Transaction> getAllTransactions();
    void savingTransaction(User user, MoneyTransferRequestDTO request);
}
