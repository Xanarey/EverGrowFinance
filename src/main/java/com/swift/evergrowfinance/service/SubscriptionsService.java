package com.swift.evergrowfinance.service;

import com.swift.evergrowfinance.model.entities.Subscription;
import com.swift.evergrowfinance.model.entities.User;
import com.swift.evergrowfinance.model.entities.Wallet;

import java.util.List;

public interface SubscriptionsService {
    List<Subscription> getAllSubscriptions();
    void saveSubscription(Subscription subscription);
    Subscription getSubscriptionById(Long id);
    void deleteSubscription(User user, Long subscriptionId);
    void createSubscription(User user, Wallet wallet);
}
