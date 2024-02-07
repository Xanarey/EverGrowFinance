package com.swift.evergrowfinance.service.impl;

import com.swift.evergrowfinance.model.User;
import com.swift.evergrowfinance.model.Wallet;
import com.swift.evergrowfinance.repository.UserRepository;
import com.swift.evergrowfinance.repository.WalletRepository;
import com.swift.evergrowfinance.service.MoneyTransferService;
import com.swift.evergrowfinance.service.TransactionService;
import com.swift.evergrowfinance.service.WalletService;
import com.swift.evergrowfinance.exceptions.InsufficientFundsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;

@Service
@Slf4j
public class MoneyTransferServiceImpl implements MoneyTransferService {

    private final WalletRepository walletRepository;
    private final WalletService walletService;
    private final UserRepository userRepository;
    private final TransactionService transactionService;

    @Autowired
    public MoneyTransferServiceImpl(WalletRepository walletRepository, WalletService walletService, UserRepository userRepository, TransactionService transactionService) {
        this.walletRepository = walletRepository;
        this.walletService = walletService;
        this.userRepository = userRepository;
        this.transactionService = transactionService;
    }

    @Transactional
    @Override
    public void transferMoney(String fromPhoneNumber, String toPhoneNumber, BigDecimal amount) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        Wallet walletFrom = walletRepository.findByPhoneNumber(fromPhoneNumber)
                .orElseThrow(() -> new RuntimeException("Кошелек отправителя не найден"));

        Wallet walletTo = walletRepository.findByPhoneNumber(toPhoneNumber)
                .orElseThrow(() -> new RuntimeException("Кошелек получателя не найден"));

        boolean walletExists = user.getWallets().stream()
                .anyMatch(wallet -> fromPhoneNumber.equals(wallet.getPhoneNumber()));

        if (!walletExists) {
            throw new RuntimeException("Невозможно совершить операцию по указанным данным");
        }

        if (fromPhoneNumber.equals(toPhoneNumber)) {
            throw new RuntimeException("Невозможно перевести средства самому себе");
        }

        if (walletFrom.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Недостаточно средств на счету для перевода");
        }

        walletFrom.setBalance(walletFrom.getBalance().subtract(amount));
        Objects.requireNonNull(walletTo).setBalance(walletTo.getBalance().add(amount));

        walletService.update(walletFrom);
        walletService.update(walletTo);
        transactionService.savingTransaction(user.getId(), walletTo.getUser().getId(), amount, walletFrom.getWalletType(), walletFrom.getPhoneNumber());

        log.info("IN MoneyTransferServiceImpl transferMoney - close transaction");
    }

}
