package com.swift.evergrowfinance.repository;

import com.swift.evergrowfinance.model.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> getAllById(Long fromAccountId);
}
