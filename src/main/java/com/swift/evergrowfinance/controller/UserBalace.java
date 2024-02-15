package com.swift.evergrowfinance.controller;

import com.swift.evergrowfinance.dto.UserBalanceDto;
import com.swift.evergrowfinance.model.User;
import com.swift.evergrowfinance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/balance")
public class UserBalace {

    private final UserService userService;

    @Autowired
    public UserBalace(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<List<UserBalanceDto>> getUserBalance() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        List<UserBalanceDto> userBalanceDtoList = userService.getUserByEmail(userEmail)
                .map(User::getWallets)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + userEmail + " does not exist"))
                .stream()
                .map(w -> UserBalanceDto.builder()
                        .phoneNumber(w.getPhoneNumber())
                        .walletType(w.getWalletType().name())
                        .balance(w.getBalance())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(userBalanceDtoList);
    }
}
