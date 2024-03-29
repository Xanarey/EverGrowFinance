package com.swift.evergrowfinance.repository;

import com.swift.evergrowfinance.model.entities.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Subscription getSubscriptionById(Long id);
}
