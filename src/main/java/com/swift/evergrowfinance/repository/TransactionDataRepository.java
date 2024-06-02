package com.swift.evergrowfinance.repository;

import com.swift.evergrowfinance.model.entities.TransactionData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionDataRepository extends JpaRepository<TransactionData, Long> {
    TransactionData findTransactionDataByUserId(Long id);
    void deleteTransactionDataByUserId(Long id);
}
