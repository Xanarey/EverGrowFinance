package com.swift.evergrowfinance.service.impl;

import com.swift.evergrowfinance.dto.MoneyTransferRequestDTO;
import com.swift.evergrowfinance.exceptions.InsufficientFundsException;
import com.swift.evergrowfinance.exceptions.InvalidTransactionException;
import com.swift.evergrowfinance.exceptions.WalletNotFoundException;
import com.swift.evergrowfinance.model.entities.TransactionData;
import com.swift.evergrowfinance.model.entities.User;
import com.swift.evergrowfinance.model.entities.Wallet;
import com.swift.evergrowfinance.repository.WalletRepository;
import com.swift.evergrowfinance.service.MoneyTransferService;
import com.swift.evergrowfinance.service.TransactionService;
import com.swift.evergrowfinance.service.UserService;
import com.swift.evergrowfinance.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Slf4j
public class TransferServiceImpl implements MoneyTransferService {

    private static final String PHONE_REGEX = "(8|\\+7)\\d{10}";
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);

    private final WalletRepository walletRepository;
    private final WalletService walletService;
    private final UserService userService;
    private final TransactionService transactionService;

    @Autowired
    public TransferServiceImpl(WalletRepository walletRepository, WalletService walletService, UserService userService, TransactionService transactionService) {
        this.walletRepository = walletRepository;
        this.walletService = walletService;
        this.userService = userService;
        this.transactionService = transactionService;
    }

//    @CachePut(value = "users", key = "#user.id")
    @Transactional
    @Override
    public void transferMoney(User user, TransactionData request) {
        if (!PHONE_PATTERN.matcher(request.getSenderPhoneNumber()).matches() || !PHONE_PATTERN.matcher(request.getRecipientPhoneNumber()).matches()) {
            throw new IllegalArgumentException("Неверный формат номера телефона");
        }

        if (request.getSenderPhoneNumber().equals(request.getRecipientPhoneNumber())) {
            throw new InvalidTransactionException("Невозможно перевести средства на тот же кошелек");
        }

        Optional<Wallet> optionalWallet = walletRepository.findByPhoneNumber(request.getSenderPhoneNumber());
        Wallet walletFrom;
        if (optionalWallet.isPresent()) {
            walletFrom = optionalWallet.get();
        } else {
            throw new WalletNotFoundException("Кошелек отправителя не найден");
        }

        Wallet walletTo = walletRepository.findByPhoneNumber(request.getRecipientPhoneNumber())
                .orElseThrow(() -> new WalletNotFoundException("Кошелек получателя не найден"));

        boolean walletExists = user.getWallets().stream()
                .anyMatch(wallet -> request.getSenderPhoneNumber().equals(wallet.getPhoneNumber()));

        if (!walletExists) {
            throw new InvalidTransactionException("Невозможно совершить операцию по указанным данным");
        }

        if (walletFrom.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientFundsException("Недостаточно средств на счету для перевода");
        }



        walletFrom.setBalance(walletFrom.getBalance().subtract(request.getAmount()));
        walletTo.setBalance(walletTo.getBalance().add(request.getAmount()));

        walletService.update(walletFrom);
        walletService.update(walletTo);


        User one = walletFrom.getUser();
        User two = walletTo.getUser();

        userService.update(one);
        userService.update(two);

        MoneyTransferRequestDTO moneyTransferRequestDTO = new MoneyTransferRequestDTO(
                user, request.getAmount(), request.getCurrency(), request.getSenderPhoneNumber(), request.getRecipientPhoneNumber(), request.getDescription(), request.getType()
        );

        transactionService.savingTransaction(one, moneyTransferRequestDTO);




        log.info("Перевод средств выполнен: с {} на {}, сумма: {}", request.getSenderPhoneNumber(), request.getRecipientPhoneNumber(),request.getAmount());
    }

}
