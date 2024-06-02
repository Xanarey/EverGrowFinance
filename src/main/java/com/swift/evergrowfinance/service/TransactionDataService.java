package com.swift.evergrowfinance.service;

import com.swift.evergrowfinance.model.entities.TransactionData;
import com.swift.evergrowfinance.repository.TransactionDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionDataService {

    private final TransactionDataRepository transactionDataRepository;

    @Autowired
    public TransactionDataService(TransactionDataRepository transactionDataRepository) {
        this.transactionDataRepository = transactionDataRepository;
    }

    public void saveTransaction(TransactionData transactionData) {
        transactionDataRepository.save(transactionData);
    }

    public TransactionData getTransactionById(Long id) {
        return transactionDataRepository.findTransactionDataByUserId(id);
    }

    @Transactional
    public void deleteTransactionById(Long id) {
        transactionDataRepository.deleteTransactionDataByUserId(id);
    }
}
