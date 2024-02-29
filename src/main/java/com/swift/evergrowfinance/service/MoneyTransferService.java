package com.swift.evergrowfinance.service;

import com.swift.evergrowfinance.dto.MoneyTransferRequest;
import com.swift.evergrowfinance.model.entities.User;
import com.swift.evergrowfinance.model.entities.Wallet;

import java.math.BigDecimal;

public interface MoneyTransferService {
    void transferMoney(User user, MoneyTransferRequest request);
}