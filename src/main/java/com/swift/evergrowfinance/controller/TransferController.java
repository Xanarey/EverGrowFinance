package com.swift.evergrowfinance.controller;

import com.swift.evergrowfinance.dto.ConfirmTransferRequestDTO;
import com.swift.evergrowfinance.dto.MoneyTransferRequestDTO;
import com.swift.evergrowfinance.dto.TransferResponseDTO;
import com.swift.evergrowfinance.model.entities.TransactionData;
import com.swift.evergrowfinance.model.entities.User;
import com.swift.evergrowfinance.service.MoneyTransferService;
import com.swift.evergrowfinance.service.OtpService;
import com.swift.evergrowfinance.service.TransactionDataService;
import com.swift.evergrowfinance.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.swift.evergrowfinance.model.enums.TransactionStatus.PENDING;

@Slf4j
@RestController
@RequestMapping("/transfers")
public class TransferController {

    private final MoneyTransferService moneyTransferService;
    private final UserService userService;
    private final OtpService otpService;
    private final TransactionDataService transactionDataService;

    @Autowired
    public TransferController(MoneyTransferService moneyTransferService, UserService userService, OtpService otpService, TransactionDataService transactionDataService) {
        this.moneyTransferService = moneyTransferService;
        this.userService = userService;
        this.otpService = otpService;
        this.transactionDataService = transactionDataService;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public TransferResponseDTO transferMoney(@RequestBody MoneyTransferRequestDTO request) {
        try {
            User userContextHolder = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

            TransactionData transactionData = new TransactionData();
            transactionData.setUserId(userContextHolder.getId());
            transactionData.setAmount(request.getAmount());
            transactionData.setCurrency(request.getCurrency());
            transactionData.setDateTime(LocalDateTime.now().plusMinutes(10));
            transactionData.setSenderPhoneNumber(request.getSenderPhoneNumber());
            transactionData.setRecipientPhoneNumber(request.getRecipientPhoneNumber());
            transactionData.setStatus(PENDING);
            transactionData.setType(request.getType());
            transactionData.setDescription(request.getDescription());

            if (request.getAmount().compareTo(new BigDecimal("10000")) > 0) {
                String otp = otpService.generateOtp(userContextHolder);
                otpService.sendOtp(userContextHolder, otp);
                transactionDataService.saveTransaction(transactionData);
                return new TransferResponseDTO("Требуется ввод одноразового кода", true, "TEST");
            } else {
                moneyTransferService.transferMoney(userContextHolder, transactionData);
                return new TransferResponseDTO("Перевод успешно выполнен");
            }
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/confirm")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public TransferResponseDTO confirmTransfer(@RequestBody ConfirmTransferRequestDTO request) {
        try {
            User userContextHolder = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

            boolean isOtpValid = otpService.validateOtp(userContextHolder, request.getOtp());
            if (!isOtpValid) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid OTP");
            }

            TransactionData transactionData = transactionDataService.getTransactionById(userContextHolder.getId());
            moneyTransferService.transferMoney(userContextHolder, transactionData);
            transactionDataService.deleteTransactionById(userContextHolder.getId());
            return new TransferResponseDTO("Перевод успешно выполнен");
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
