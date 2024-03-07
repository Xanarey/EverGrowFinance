package com.swift.evergrowfinance.controller;

import com.swift.evergrowfinance.dto.TransactionDTO;
import com.swift.evergrowfinance.model.entities.Transaction;
import com.swift.evergrowfinance.model.entities.User;
import com.swift.evergrowfinance.service.TransactionService;
import com.swift.evergrowfinance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final UserService userService;

    @Autowired
    public TransactionController(TransactionService transactionService, UserService userService) {
        this.transactionService = transactionService;
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public List<Transaction> getUserTransactionsHistory(@PathVariable Long id) {
        User user = userService.getUserServById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return transactionService.getTransactionsForUser(user.getId());
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public List<TransactionDTO> getMyTransactionsHistory() {
        User user = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        List<TransactionDTO> transactionDTOS =
                transactionService.getTransactionsForUser(user.getId()).stream()
                        .map(TransactionDTO::transactionDTO)
                        .toList();
        transactionDTOS = transactionDTOS.stream()
                .sorted(Comparator.comparing(TransactionDTO::getDateTime).reversed())
                .toList();
        return transactionDTOS;
    }
}
