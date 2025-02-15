package com.swift.evergrowfinance.service.impl;

import com.swift.evergrowfinance.dto.MoneyTransferRequestDTO;
import com.swift.evergrowfinance.dto.TransactionDTO;
import com.swift.evergrowfinance.model.entities.Transaction;
import com.swift.evergrowfinance.model.entities.User;
import com.swift.evergrowfinance.repository.TransactionRepository;
import com.swift.evergrowfinance.service.TransactionService;
import com.swift.evergrowfinance.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static com.swift.evergrowfinance.model.enums.TransactionStatus.COMPLETED;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final TransactionService transactionService;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, UserService userService, TransactionService transactionService) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.transactionService = transactionService;
    }

    public List<TransactionDTO> getTransactionDTOS() {
        List<TransactionDTO> transactionDTOS =
                transactionService.getTransactionsForUser(
                                userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")).getId())
                        .stream()
                        .map(TransactionDTO::transactionDTO)
                        .toList();
        transactionDTOS = transactionDTOS.stream()
                .sorted(Comparator.comparing(TransactionDTO::getDateTime).reversed())
                .toList();
        return transactionDTOS;
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
