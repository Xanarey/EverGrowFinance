package com.swift.evergrowfinance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<com.swift.evergrowfinance.model.Transaction, Long> {
}
