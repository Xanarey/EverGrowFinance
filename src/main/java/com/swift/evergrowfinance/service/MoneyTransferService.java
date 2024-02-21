package com.swift.evergrowfinance.service;

import com.swift.evergrowfinance.model.User;
import com.swift.evergrowfinance.model.Wallet;

import java.math.BigDecimal;

public interface MoneyTransferService {

    void transferMoney(User user, String fromPhoneNumber, String toPhoneNumber, BigDecimal amount);
    void initiateSubscription(User user, Wallet wallet);

}