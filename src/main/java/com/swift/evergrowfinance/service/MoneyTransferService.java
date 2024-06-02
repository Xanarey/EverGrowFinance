package com.swift.evergrowfinance.service;

import com.swift.evergrowfinance.model.entities.TransactionData;
import com.swift.evergrowfinance.model.entities.User;

public interface MoneyTransferService {
    void transferMoney(User user, TransactionData request);
}