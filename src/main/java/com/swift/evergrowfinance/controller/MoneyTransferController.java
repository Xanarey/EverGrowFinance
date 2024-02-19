package com.swift.evergrowfinance.controller;

import com.swift.evergrowfinance.dto.MoneyTransferRequest;
import com.swift.evergrowfinance.dto.TransferResponse;
import com.swift.evergrowfinance.service.MoneyTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/transfer")
public class MoneyTransferController {

    private final MoneyTransferService moneyTransferService;

    @Autowired
    public MoneyTransferController(MoneyTransferService moneyTransferService) {
        this.moneyTransferService = moneyTransferService;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public TransferResponse transferMoney(@RequestBody MoneyTransferRequest request) {
        try {
            moneyTransferService.transferMoney(request.getFromPhoneNumber(), request.getToPhoneNumber(), request.getAmount());
            return new TransferResponse("Перевод успешно выполнен");
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
