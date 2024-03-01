package com.swift.evergrowfinance.service;

import com.swift.evergrowfinance.dto.MoneyTransferRequestDTO;
import com.swift.evergrowfinance.model.entities.User;

public interface MoneyTransferService {
    void transferMoney(User user, MoneyTransferRequestDTO request);
}