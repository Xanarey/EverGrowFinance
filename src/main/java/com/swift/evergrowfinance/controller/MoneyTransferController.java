package com.swift.evergrowfinance.controller;

import com.swift.evergrowfinance.dto.MoneyTransferRequest;
import com.swift.evergrowfinance.dto.TransferResponse;
import com.swift.evergrowfinance.model.entities.User;
import com.swift.evergrowfinance.service.MoneyTransferService;
import com.swift.evergrowfinance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/transfer")
public class MoneyTransferController {

    private final MoneyTransferService moneyTransferService;
    private final UserService userService;

    @Autowired
    public MoneyTransferController(MoneyTransferService moneyTransferService, UserService userService) {
        this.moneyTransferService = moneyTransferService;
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public TransferResponse transferMoney(@RequestBody MoneyTransferRequest request) {
        try {
            User userContextHolder = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

            User user = userService.getUserServById(userContextHolder.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found by ID"));

            moneyTransferService.transferMoney(user, request);
            return new TransferResponse("Перевод успешно выполнен");
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
