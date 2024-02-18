package com.swift.evergrowfinance.service;

import com.swift.evergrowfinance.model.Subscription;

public interface SubscriptionsService {
    void saveSubscription(Subscription subscription);
    Subscription getSubscriptionById(Long id);
    void deleteSubscriptionById(Long id);
}
