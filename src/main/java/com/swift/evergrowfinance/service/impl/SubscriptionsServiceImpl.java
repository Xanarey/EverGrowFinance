package com.swift.evergrowfinance.service.impl;

import com.swift.evergrowfinance.model.Subscription;
import com.swift.evergrowfinance.model.User;
import com.swift.evergrowfinance.repository.SubscriptionRepository;
import com.swift.evergrowfinance.service.SubscriptionsService;
import com.swift.evergrowfinance.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
public class SubscriptionsServiceImpl implements SubscriptionsService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserService userService;

    @Autowired
    public SubscriptionsServiceImpl(SubscriptionRepository subscriptionRepository, UserService userService) {
        this.subscriptionRepository = subscriptionRepository;
        this.userService = userService;
    }

    @Override
    public Subscription getSubscriptionById(Long id) {
        log.info("IN SubscriptionsServiceImpl in getSubscriptionById + {} ", id);
        return subscriptionRepository.getSubscriptionById(id);
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
