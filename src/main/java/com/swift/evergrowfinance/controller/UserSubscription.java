package com.swift.evergrowfinance.controller;

import com.swift.evergrowfinance.dto.SubscriptionRequest;
import com.swift.evergrowfinance.model.Subscription;
import com.swift.evergrowfinance.model.User;
import com.swift.evergrowfinance.model.Wallet;
import com.swift.evergrowfinance.service.MoneyTransferService;
import com.swift.evergrowfinance.service.SubscriptionsService;
import com.swift.evergrowfinance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/subscriptions")
public class UserSubscription {

    private final UserService userService;
    private final MoneyTransferService moneyTransferService;
    private final SubscriptionsService subscriptionsService;

    @Autowired
    public UserSubscription(UserService userService, MoneyTransferService moneyTransferService, SubscriptionsService subscriptionsService) {
        this.userService = userService;
        this.moneyTransferService = moneyTransferService;
        this.subscriptionsService = subscriptionsService;
    }

    @PostMapping("/initiate")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> createKinopoiskSubscription(@RequestBody SubscriptionRequest request) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userOptional = userService.getUserByEmail(userEmail);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>("Пользователь не найден", HttpStatus.BAD_REQUEST);
        }

        User user = userOptional.get();
        String phoneNumber = request.getPhoneNumber();

        Optional<Wallet> walletOptional = user.getWallets().stream()
                .filter(w -> w.getPhoneNumber().equals(phoneNumber))
                .findFirst();
        if (walletOptional.isEmpty()) {
            return new ResponseEntity<>("Указаны не валидные данные", HttpStatus.BAD_REQUEST);
        }

        Optional<Subscription> subscriptionOptional = user.getSubscriptions().stream()
                .filter(s -> s.getName().equals("Kinopoisk"))
                .findFirst();
        if (subscriptionOptional.isPresent()) {
            return new ResponseEntity<>("У вас уже имеется действующая подписка на Кинопоиск", HttpStatus.OK);
        }

        Wallet wallet = walletOptional.get();
        if (wallet.getBalance().compareTo(new BigDecimal("500.00")) < 0) {
            return new ResponseEntity<>("На счету вашего кошелька недостаточно средств для осуществления подписки на сервис 'Кинопоиск'", HttpStatus.OK);
        }

        moneyTransferService.initiateSubscription(user, phoneNumber);

        return new ResponseEntity<>("Подписка на Кинопоиск успешно оформлена по номеру - " + phoneNumber, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteSubscription(@PathVariable Long id) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userOptional = userService.getUserByEmail(userEmail);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>("Пользователь не найден", HttpStatus.BAD_REQUEST);
        }

        User user = userOptional.get();
        Optional<Subscription> subscriptionOptional = user.getSubscriptions().stream()
                .filter(s -> s.getId().equals(id))
                .findFirst();
        if (subscriptionOptional.isEmpty())
            return new ResponseEntity<>("Подписка с указанным id не найдена", HttpStatus.OK);
        Subscription subscription = subscriptionOptional.get();

        user.getSubscriptions().removeIf(s -> s.getId().equals(subscription.getId()));
        userService.update(user);

        subscriptionsService.deleteSubscriptionById(subscription.getId());



        return new ResponseEntity<>("Подписка на " + subscription.getName() + " успешно удалена", HttpStatus.OK);
    }
}
