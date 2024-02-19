package com.swift.evergrowfinance.service.impl;

import com.swift.evergrowfinance.model.Subscription;
import com.swift.evergrowfinance.model.User;
import com.swift.evergrowfinance.repository.SubscriptionRepository;
import com.swift.evergrowfinance.repository.UserRepository;
import com.swift.evergrowfinance.service.SubscriptionsService;
import com.swift.evergrowfinance.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
public class SubscriptionsServiceImpl implements SubscriptionsService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public SubscriptionsServiceImpl(SubscriptionRepository subscriptionRepository, UserService userService, UserRepository userRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @CacheEvict(value = "subscriptions", key = "#subscription.id")
    @Override
    public void saveSubscription(Subscription subscription) {
        log.info("IN SubscriptionsServiceImpl in saveSubscription + {} ", subscription);
        subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription getSubscriptionById(Long id) {
        log.info("IN SubscriptionsServiceImpl in getSubscriptionById + {} ", id);
        return subscriptionRepository.getSubscriptionById(id);
    }

    @CacheEvict(value = "subscriptions", key = "#subscriptionId")
    @Override
    public void deleteSubscription(Long userId, Long subscriptionId) {
        log.info("IN SubscriptionsServiceImpl in deleteSubscriptionById {}", subscriptionId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        Subscription subscription = user.getSubscriptions().stream()
                .filter(s -> s.getId().equals(subscriptionId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Подписка с указанным id не найдена"));

        user.getSubscriptions().removeIf(s -> s.getId().equals(subscription.getId()));
        userService.update(user);

        subscriptionRepository.deleteById(subscription.getId());
    }
}
