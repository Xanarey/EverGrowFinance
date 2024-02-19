package com.swift.evergrowfinance.service.impl;

import com.swift.evergrowfinance.model.Wallet;
import com.swift.evergrowfinance.repository.WalletRepository;
import com.swift.evergrowfinance.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    @Autowired
    public WalletServiceImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Transactional
    @Override
    public void update(Wallet wallet) {
        log.info("IN WalletServiceImpl update {}", wallet);
        walletRepository.save(wallet);
    }
}
