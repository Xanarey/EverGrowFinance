package com.swift.evergrowfinance.controller;

import com.swift.evergrowfinance.dto.UserBalanceDto;
import com.swift.evergrowfinance.model.User;
import com.swift.evergrowfinance.model.Wallet;
import com.swift.evergrowfinance.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/balance")
public class UserBalance {

    private final UserService userService;

    @Autowired
    public UserBalance(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public List<UserBalanceDto> getUserBalance(@PathVariable Long id) {
        User user = userService.getUserServById(id)
                .orElseThrow(() -> {
                    log.error("User with id {} not found", id);
                    return new UsernameNotFoundException("User with id " + id + " not found");
                });

        log.info("Retrieving balance for user with id {} ", id);
        return user.getWallets().stream()
                .map(this::toUserBalanceDto)
                .toList();
    }

    private UserBalanceDto toUserBalanceDto(Wallet wallet) {
        return UserBalanceDto.builder()
                .phoneNumber(wallet.getPhoneNumber())
                .walletType(wallet.getWalletType().name())
                .balance(wallet.getBalance())
                .build();
    }
}
