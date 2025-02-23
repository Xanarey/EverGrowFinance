package com.swift.evergrowfinance.controllers;

import com.swift.evergrowfinance.service.products.BinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crypto")
public class CryptoController {

    private final BinanceService binanceService;

    @Autowired
    public CryptoController(BinanceService binanceService) {
        this.binanceService = binanceService;
    }

    @GetMapping("/btc")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getBitcoinPrice() {
        return binanceService.getBitcoinPrice();
    }
}