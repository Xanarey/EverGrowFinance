package com.swift.evergrowfinance.service;

import com.swift.evergrowfinance.model.Subscription;
import com.swift.evergrowfinance.model.User;

public interface SubscriptionsService {
    void saveSubscription(User user, Subscription subscription);
    Subscription getSubscriptionById(Long id);
    void deleteSubscription(User user, Long subscriptionId);
}
