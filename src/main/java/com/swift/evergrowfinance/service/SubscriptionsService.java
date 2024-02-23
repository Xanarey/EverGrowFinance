package com.swift.evergrowfinance.service;

import com.swift.evergrowfinance.model.Subscription;
import com.swift.evergrowfinance.model.User;

import java.util.List;

public interface SubscriptionsService {
    List<Subscription> getAllSubscriptions();
    void saveSubscription(Subscription subscription);
    Subscription getSubscriptionById(Long id);
    void deleteSubscription(User user, Long subscriptionId);
}
