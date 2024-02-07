package com.swift.evergrowfinance.service.impl;

import com.swift.evergrowfinance.model.Transaction;
import com.swift.evergrowfinance.model.WalletType;
import com.swift.evergrowfinance.repository.TransactionRepository;
import com.swift.evergrowfinance.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        log.info("IN TransactionServiceImpl TransactionServiceImpl");
        return transactionRepository.findAll();
    }

    @Override
    public void savingTransaction(Long fromAccountId, Long toAccountId, BigDecimal amount, WalletType walletType, String phoneNumber) {
        Transaction transaction = new Transaction();
        transaction.setFromAccountId(fromAccountId);
        transaction.setToAccountId(toAccountId);
        transaction.setAmount(amount);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setWalletType(walletType);
        transaction.setPhoneNumber(phoneNumber);
        transactionRepository.save(transaction);
        log.info("IN TransactionServiceImpl savingTransaction {}", transaction);
    }
}
