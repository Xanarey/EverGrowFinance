package com.swift.evergrowfinance.service.impl;

import com.swift.evergrowfinance.dto.MoneyTransferRequestDTO;
import com.swift.evergrowfinance.model.entities.Transaction;
import com.swift.evergrowfinance.repository.TransactionRepository;
import com.swift.evergrowfinance.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.swift.evergrowfinance.model.enums.TransactionStatus.COMPLETED;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> getTransactionsForUser(Long id) {
        log.info("IN TransactionServiceImpl getTransactionById {}", id);
        return transactionRepository.getAllById(id);
    }

    @Override
    public List<Transaction> getAllTransactions() {
        log.info("IN TransactionServiceImpl TransactionServiceImpl");
        return transactionRepository.findAll();
    }

    @Transactional
    @Override
    public void savingTransaction(MoneyTransferRequestDTO request) {
        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setCurrency(request.getCurrency());
        transaction.setDateTime(LocalDateTime.now());
        transaction.setSenderPhoneNumber(request.getSenderPhoneNumber());
        transaction.setRecipientPhoneNumber(request.getRecipientPhoneNumber());
        transaction.setStatus(COMPLETED); // TODO Как правильно реализовать с кафкой ?
        transaction.setType(request.getType());
        transaction.setDescription(request.getDescription());

        transactionRepository.save(transaction);
        log.info("IN TransactionServiceImpl savingTransaction {}", transaction);
    }
}
