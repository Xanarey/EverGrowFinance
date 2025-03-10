package com.swift.evergrowfinance.service.crypto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArbitrageService {

    private final BinanceService binanceService;
    private final HuobiService huobiService;

    @Autowired
    public ArbitrageService(BinanceService binanceService, HuobiService huobiService) {
        this.binanceService = binanceService;
        this.huobiService = huobiService;
    }

    public String findArbitrageOpportunities(double amountUsd) {
        double resultBinance = amountUsd / binanceService.findBestAdvertisement(amountUsd, true).getPrice();
        double resultHuobi = amountUsd / huobiService.findBestAdvertisement(amountUsd).getPrice();
        if (resultBinance > resultHuobi) {
            return "RESULT Binance -> Huobi % : " + resultBinance / resultHuobi + " | 1 roundtrip arbitrage : " + (amountUsd * 0.01) * (resultBinance / resultHuobi);
        } else return "RESULT Huobi -> Binance % : " + resultHuobi / resultBinance + " (1 roundtrip arbitrage : )" + (amountUsd * 0.01) * (resultBinance / resultHuobi);
    }
}
