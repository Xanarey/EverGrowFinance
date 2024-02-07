package com.swift.evergrowfinance.service;

import java.math.BigDecimal;

public interface MoneyTransferService {

    void transferMoney(String fromPhoneNumber, String toPhoneNumber, BigDecimal amount);

}