package com.swift.evergrowfinance.controllers;

import com.swift.evergrowfinance.dto.crypto.CurrencyPairDTO;
import com.swift.evergrowfinance.service.crypto.ArbitrageService;
import com.swift.evergrowfinance.service.crypto.BinanceService;
import com.swift.evergrowfinance.service.crypto.HuobiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crypto")
public class CryptoController {

    private final BinanceService binanceService;
    private final HuobiService huobiService;
    private final ArbitrageService arbitrageService;

    @Autowired
    public CryptoController(BinanceService binanceService, HuobiService huobiService, ArbitrageService arbitrageService) {
        this.binanceService = binanceService;
        this.huobiService = huobiService;
        this.arbitrageService = arbitrageService;
    }


    @GetMapping("/binance/btc/{amountUsd}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CurrencyPairDTO> getBinanceBTCPrice(@PathVariable double amountUsd) {
        CurrencyPairDTO bestAdv = binanceService.findBestAdvertisement(amountUsd);
        System.out.println("BEST ADVERTISEMENT getBinanceBTCPrice: " + bestAdv);
        return ResponseEntity.ok(bestAdv);
    }

    @GetMapping("/huobi/btc/{amountUsd}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CurrencyPairDTO> getHuobiBTCPrice(@PathVariable double amountUsd) {
        CurrencyPairDTO bestAdv = huobiService.findBestAdvertisement(amountUsd);
        System.out.println("BEST ADVERTISEMENT getHuobiBTCPrice: " + bestAdv);
        return ResponseEntity.ok(bestAdv);
    }

    @GetMapping("/arbitrage/{amountUsd}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getArbitrageBTCPrice(@PathVariable double amountUsd) {
        return arbitrageService.findArbitrageOpportunities(amountUsd);
    }
}