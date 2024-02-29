package com.swift.evergrowfinance.service;

import com.swift.evergrowfinance.dto.MoneyTransferRequest;
import com.swift.evergrowfinance.model.entities.Transaction;
import com.swift.evergrowfinance.model.enums.Currency;
import com.swift.evergrowfinance.model.enums.TransactionType;
import com.swift.evergrowfinance.model.enums.WalletType;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {

    List<Transaction> getTransactionsForUser(Long id);
    List<Transaction> getAllTransactions();
    void savingTransaction(MoneyTransferRequest request);
}
