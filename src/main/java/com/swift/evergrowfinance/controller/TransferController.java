package com.swift.evergrowfinance.controller;

import com.swift.evergrowfinance.dto.ConfirmTransferRequestDTO;
import com.swift.evergrowfinance.dto.MoneyTransferRequestDTO;
import com.swift.evergrowfinance.dto.TransferResponseDTO;
import com.swift.evergrowfinance.model.entities.User;
import com.swift.evergrowfinance.service.MoneyTransferService;
import com.swift.evergrowfinance.service.OtpService;
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

import java.math.BigDecimal;

@RestController
@RequestMapping("/transfers")
public class TransferController {

    private static final BigDecimal THRESHOLD = new BigDecimal("10000");
    private static MoneyTransferRequestDTO moneyTransferRequestDTO;
    private final MoneyTransferService moneyTransferService;
    private final UserService userService;
    private final OtpService otpService;

    @Autowired
    public TransferController(MoneyTransferService moneyTransferService, UserService userService, OtpService otpService) {
        this.moneyTransferService = moneyTransferService;
        this.userService = userService;
        this.otpService = otpService;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public TransferResponseDTO transferMoney(@RequestBody MoneyTransferRequestDTO request) {
        try {
            User userContextHolder = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

            if (request.getAmount().compareTo(THRESHOLD) > 0) {


                String otp = otpService.generateOtp(userContextHolder);
                otpService.sendOtp(userContextHolder, otp);

                moneyTransferRequestDTO = request;
                return new TransferResponseDTO("Требуется ввод одноразового кода", true, "TEST");
            } else {
                moneyTransferService.transferMoney(userContextHolder, request);
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

            moneyTransferService.transferMoney(userContextHolder, moneyTransferRequestDTO);
            return new TransferResponseDTO("Перевод успешно выполнен");
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
