package com.swift.evergrowfinance.controller;

import com.swift.evergrowfinance.dto.SubscriptionRequest;
import com.swift.evergrowfinance.model.entities.User;
import com.swift.evergrowfinance.model.entities.Wallet;
import com.swift.evergrowfinance.service.SubscriptionsService;
import com.swift.evergrowfinance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/subscriptions")
public class UserSubscription {

    private final UserService userService;
    private final SubscriptionsService subscriptionsService;

    @Autowired
    public UserSubscription(UserService userService, SubscriptionsService subscriptionsService) {
        this.userService = userService;
        this.subscriptionsService = subscriptionsService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String createKinopoiskSubscription(@RequestBody SubscriptionRequest request) {
        User userContextHolder = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        User user = userService.getUserServById(userContextHolder.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found by ID"));

        Wallet wallet = validateAndGetWallet(user, request.getPhoneNumber());
        validateSubscription(user);
        validateWalletBalance(wallet, new BigDecimal("500.00"));

        subscriptionsService.createSubscription(user, wallet);

        return "Подписка на Кинопоиск успешно оформлена по номеру - " + request.getPhoneNumber();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteSubscription(@PathVariable Long id) {
        User user = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        subscriptionsService.deleteSubscription(user, id);
        return "Подписка успешно удалена";
    }

    private Wallet validateAndGetWallet(User user, String phoneNumber) {
        return user.getWallets().stream()
                .filter(w -> w.getPhoneNumber().equals(phoneNumber))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Указаны не валидные данные"));
    }
    private void validateSubscription(User user) {
        user.getSubscriptions().stream()
                .filter(s -> s.getName().equals("Kinopoisk"))
                .findFirst()
                .ifPresent(s -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "У вас уже имеется подписка на " + "Kinopoisk");
                });
    }

    private void validateWalletBalance(Wallet wallet, BigDecimal requiredBalance) {
        if (wallet.getBalance().compareTo(requiredBalance) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "На счету вашего кошелька недостаточно средств для осуществления подписки на сервис 'Кинопоиск'");
        }
    }
}
