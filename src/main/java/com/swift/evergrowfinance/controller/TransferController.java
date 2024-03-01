package com.swift.evergrowfinance.controller;

import com.swift.evergrowfinance.dto.MoneyTransferRequestDTO;
import com.swift.evergrowfinance.dto.TransferResponseDTO;
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

@RestController
@RequestMapping("/transfers")
public class TransferController {

    private final MoneyTransferService moneyTransferService;
    private final UserService userService;

    @Autowired
    public TransferController(MoneyTransferService moneyTransferService, UserService userService) {
        this.moneyTransferService = moneyTransferService;
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public TransferResponseDTO transferMoney(@RequestBody MoneyTransferRequestDTO request) {
        try {
            User userContextHolder = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

            moneyTransferService.transferMoney(userContextHolder, request);
            return new TransferResponseDTO("Перевод успешно выполнен");
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
