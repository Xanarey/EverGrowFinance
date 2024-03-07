package com.swift.evergrowfinance.service.impl;

import com.swift.evergrowfinance.dto.MoneyTransferRequestDTO;
import com.swift.evergrowfinance.model.entities.Transaction;
import com.swift.evergrowfinance.model.entities.User;
import com.swift.evergrowfinance.repository.TransactionRepository;
import com.swift.evergrowfinance.service.TransactionService;
import com.swift.evergrowfinance.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.swift.evergrowfinance.model.enums.TransactionStatus.COMPLETED;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, UserService userService) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
    }

    public List<Transaction> getTransactionsForUser(Long id) {
        log.info("IN TransactionServiceImpl getTransactionById {}", id);
        return transactionRepository.getAllByUserId(id);
    }

    @Override
    public List<Transaction> getAllTransactions() {
        log.info("IN TransactionServiceImpl TransactionServiceImpl");
        return transactionRepository.findAll();
    }

    @Transactional
    @Override
    public void savingTransaction(User user, MoneyTransferRequestDTO request) {
        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setCurrency(request.getCurrency());
        transaction.setDateTime(LocalDateTime.now());
        transaction.setSenderPhoneNumber(request.getSenderPhoneNumber());
        transaction.setRecipientPhoneNumber(request.getRecipientPhoneNumber());
        transaction.setStatus(COMPLETED);
        transaction.setType(request.getType());
        transaction.setDescription(request.getDescription());


        transaction.setUser(user);

        transactionRepository.save(transaction);
        user.getTransactions().add(transaction);
        userService.update(user);

        log.info("IN TransactionServiceImpl savingTransaction {}", transaction);
    }
}
