package com.swift.evergrowfinance.controller;

import com.swift.evergrowfinance.dto.UserBalanceDTO;
import com.swift.evergrowfinance.model.entities.User;
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
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/balance")
public class UserBalanceController {

    private final UserService userService;

    @Autowired
    public UserBalanceController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public List<UserBalanceDTO> getUserBalance(@PathVariable Long id) {
        User user = userService.getUserServById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User with id " + id + " not found"));

        return user.getWallets().stream()
                .map(UserBalanceDTO::fromWallet)
                .collect(Collectors.toList());
    }
}