package com.swift.evergrowfinance.service;

import com.swift.evergrowfinance.model.User;

import java.math.BigDecimal;

public interface MoneyTransferService {

    void transferMoney(String fromPhoneNumber, String toPhoneNumber, BigDecimal amount);
    void initiateSubscription(User user, String walletNumber);

}