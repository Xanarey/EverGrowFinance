package com.swift.evergrowfinance.repository;

import com.swift.evergrowfinance.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<com.swift.evergrowfinance.model.Transaction, Long> {
    List<Transaction> getAllByFromAccountId(Long fromAccountId);
}
