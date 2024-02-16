package com.swift.evergrowfinance.service.impl;

import com.swift.evergrowfinance.model.Subscription;
import com.swift.evergrowfinance.repository.SubscriptionRepository;
import com.swift.evergrowfinance.service.SubscriptionsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SubscriptionsServiceImpl implements SubscriptionsService {

    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public SubscriptionsServiceImpl(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public void saveSubscription(Subscription subscription) {
        log.info("IN SubscriptionsServiceImpl in saveSubscription + {} ", subscription);
        subscriptionRepository.save(subscription);
    }
}
