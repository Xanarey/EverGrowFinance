package com.swift.evergrowfinance.service;

import com.swift.evergrowfinance.dto.MoneyTransferRequest;
import com.swift.evergrowfinance.model.entities.User;

public interface MoneyTransferService {
    void transferMoney(User user, MoneyTransferRequest request);
}