package com.swift.evergrowfinance.service.impl;

import com.swift.evergrowfinance.model.entities.Subscription;
import com.swift.evergrowfinance.model.entities.User;
import com.swift.evergrowfinance.model.entities.Wallet;
import com.swift.evergrowfinance.repository.SubscriptionRepository;
import com.swift.evergrowfinance.service.SubscriptionsService;
import com.swift.evergrowfinance.service.UserService;
import com.swift.evergrowfinance.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class SubscriptionsServiceImpl implements SubscriptionsService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserService userService;
    private final WalletService walletService;

    @Autowired
    public SubscriptionsServiceImpl(SubscriptionRepository subscriptionRepository, UserService userService, WalletService walletService) {
        this.subscriptionRepository = subscriptionRepository;
        this.userService = userService;
        this.walletService = walletService;
    }

    @Override
    public Subscription getSubscriptionById(Long id) {
        log.info("IN SubscriptionsServiceImpl in getSubscriptionById + {} ", id);
        return subscriptionRepository.getSubscriptionById(id);
    }

    @Override
    public List<Subscription> getAllSubscriptions() {
        log.info("IN SubscriptionsServiceImpl in getAllSubscriptions");
        return subscriptionRepository.findAll();
    }

    @Override
    public void createSubscription(User user, Wallet wallet) {
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

        saveSubscription(subscription);

        wallet.setBalance(wallet.getBalance().subtract(new BigDecimal("500.00")));
        walletService.update(wallet);

        if (!user.getSubscriptions().contains(subscription)) {
            user.getSubscriptions().add(subscription);
        }

        userService.update(user);
        log.info("Transaction for subs complete!");
    }

    @Override
    public void saveSubscription(Subscription subscription) {
        log.info("IN SubscriptionsServiceImpl in saveSubscription + {} ", subscription);
        subscriptionRepository.save(subscription);
    }

    @Override
    public void deleteSubscription(User user, Long subscriptionId) {
        log.info("IN SubscriptionsServiceImpl in deleteSubscriptionById {}", subscriptionId);

        Subscription subscription = user.getSubscriptions().stream()
                .filter(s -> s.getId().equals(subscriptionId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Подписка с указанным id не найдена"));

        user.getSubscriptions().removeIf(s -> s.getId().equals(subscription.getId()));
        userService.update(user);

        subscriptionRepository.deleteById(subscription.getId());
    }

}
