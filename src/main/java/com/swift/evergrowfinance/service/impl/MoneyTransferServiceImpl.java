package com.swift.evergrowfinance.service.impl;

import com.swift.evergrowfinance.exceptions.InvalidTransactionException;
import com.swift.evergrowfinance.exceptions.WalletNotFoundException;
import com.swift.evergrowfinance.model.Subscription;
import com.swift.evergrowfinance.model.User;
import com.swift.evergrowfinance.model.Wallet;
import com.swift.evergrowfinance.repository.UserRepository;
import com.swift.evergrowfinance.repository.WalletRepository;
import com.swift.evergrowfinance.service.*;
import com.swift.evergrowfinance.exceptions.InsufficientFundsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Slf4j
public class MoneyTransferServiceImpl implements MoneyTransferService {

    private final WalletRepository walletRepository;
    private final WalletService walletService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final TransactionService transactionService;
    private final SubscriptionsService subscriptionsService;

    @Autowired
    public MoneyTransferServiceImpl(WalletRepository walletRepository, WalletService walletService, UserService userService, UserRepository userRepository, TransactionService transactionService, SubscriptionsService subscriptionsService) {
        this.walletRepository = walletRepository;
        this.walletService = walletService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.transactionService = transactionService;
        this.subscriptionsService = subscriptionsService;
    }

    @Transactional
    @Override
    public void transferMoney(String fromPhoneNumber, String toPhoneNumber, BigDecimal amount) {
        if (fromPhoneNumber.equals(toPhoneNumber)) {
            throw new InvalidTransactionException("Невозможно перевести средства на тот же кошелек");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        Wallet walletFrom = walletRepository.findByPhoneNumber(fromPhoneNumber)
                .orElseThrow(() -> new WalletNotFoundException("Кошелек отправителя не найден"));

        Wallet walletTo = walletRepository.findByPhoneNumber(toPhoneNumber)
                .orElseThrow(() -> new WalletNotFoundException("Кошелек получателя не найден"));

        boolean walletExists = user.getWallets().stream()
                .anyMatch(wallet -> fromPhoneNumber.equals(wallet.getPhoneNumber()));

        if (!walletExists) {
            throw new InvalidTransactionException("Невозможно совершить операцию по указанным данным");
        }

        if (walletFrom.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Недостаточно средств на счету для перевода");
        }

        walletFrom.setBalance(walletFrom.getBalance().subtract(amount));
        walletTo.setBalance(walletTo.getBalance().add(amount));

        walletService.update(walletFrom);
        walletService.update(walletTo);
        transactionService.savingTransaction(user.getId(), walletTo.getUser().getId(), amount, walletFrom.getWalletType(), walletFrom.getPhoneNumber());

        userService.update(walletFrom.getUser());
        userService.update(walletTo.getUser());

        log.info("Перевод средств выполнен: с {} на {}, сумма: {}", fromPhoneNumber, toPhoneNumber, amount);
    }

    @Transactional
    @Override
    public void initiateSubscription(User user, String walletNumber) {
        Wallet wallet = user.getWallets().stream()
                .filter(w -> w.getPhoneNumber().equals(walletNumber))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid wallet number"));

        Subscription subscription = new Subscription();

        subscription.setName("Kinopoisk");
        subscription.setPrice(new BigDecimal("500.00"));
        subscription.setStartDate(LocalDateTime.now());
        subscription.setEndDate(LocalDateTime.now().plusMonths(3));
        subscription.setStatus("ACTIVE");

        subscription.setType("premium");
        subscription.setPaymentFrequency("monthly");
        subscription.setAuto_renew(true);
        subscription.setWalletNumber(wallet.getPhoneNumber());
        subscription.setUser(user);

        wallet.setBalance(wallet.getBalance().subtract(new BigDecimal("500.00")));
        walletService.update(wallet);
        subscriptionsService.saveSubscription(subscription);
        userService.update(user);
        log.info("Transaction for subs complete!");
    }

}
