package com.swift.evergrowfinance.service.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BinanceService {

    private final RestTemplate restTemplate;

    @Autowired
    public BinanceService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static final String BINANCE_URL = "https://api.binance.com/api/v3/ticker/price?symbol=BTCUSDT";

    public String getBitcoinPrice() {
        return restTemplate.getForObject(BINANCE_URL, String.class);
    }
}