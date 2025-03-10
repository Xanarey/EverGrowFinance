package com.swift.evergrowfinance.service.crypto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swift.evergrowfinance.dto.crypto.CurrencyPairDTO;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

@Service
public class BinanceService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String BINANCE_P2P_URL = "https://p2p.binance.com/bapi/c2c/v2/friendly/c2c/adv/search";

    public BinanceService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CurrencyPairDTO findBestAdvertisement(double desiredAmount, boolean isBTC) {

        Map<String, Object> requestPayload = new HashMap<>();
        requestPayload.put("page", 1);
        requestPayload.put("rows", 20);
        requestPayload.put("payTypes", Collections.emptyList());

        requestPayload.put("tradeType", "BUY");
        requestPayload.put("asset", "BTC");
        requestPayload.put("fiat", "USD");
        requestPayload.put("transAmount", String.valueOf(desiredAmount));
        requestPayload.put("merchantCheck", false);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestPayload, headers);

        ResponseEntity<byte[]> responseEntity = restTemplate.exchange(
                BINANCE_P2P_URL,
                HttpMethod.POST,
                requestEntity,
                byte[].class
        );

        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Ошибка запроса: " + responseEntity.getStatusCode());
        }

        byte[] bodyBytes = responseEntity.getBody();

        try (ByteArrayInputStream bis = new ByteArrayInputStream(bodyBytes);
             GZIPInputStream gis = new GZIPInputStream(bis);
             BufferedReader reader = new BufferedReader(new InputStreamReader(gis, StandardCharsets.UTF_8))) {

            String decompressedJson = reader.lines().collect(Collectors.joining("\n"));
            System.out.println("Полученный JSON: " + decompressedJson);

            JsonNode root = objectMapper.readTree(decompressedJson);
            JsonNode dataArray = root.path("data");
            if (!dataArray.isArray()) {
                throw new RuntimeException("Поле 'data' отсутствует или не является массивом.");
            }

            CurrencyPairDTO bestCurrencyPair = null;
            double bestPrice = Double.MAX_VALUE;

            for (JsonNode item : dataArray) {
                JsonNode advNode = item.path("adv");
                if (advNode.isMissingNode()) {
                    continue;
                }

                String tradeType = advNode.path("tradeType").asText();
                if (!"SELL".equalsIgnoreCase(tradeType)) {
                    continue;
                }

                double price = advNode.path("price").asDouble();
                double minAmount = advNode.path("minSingleTransAmount").asDouble();
                double maxAmount = advNode.path("maxSingleTransAmount").asDouble();

                if (desiredAmount >= minAmount && desiredAmount <= maxAmount) {
                    if (price < bestPrice) {
                        bestPrice = price;
                        bestCurrencyPair = new CurrencyPairDTO();
                        bestCurrencyPair.setAdvNo(advNode.path("advNo").asText());
                        bestCurrencyPair.setPrice(price);
                        bestCurrencyPair.setMinAmount(minAmount);
                        bestCurrencyPair.setMaxAmount(maxAmount);
                        bestCurrencyPair.setAsset(advNode.path("asset").asText());
                        bestCurrencyPair.setFiatUnit(advNode.path("fiatUnit").asText());

                        JsonNode advertiserNode = item.path("advertiser");
                        if (!advertiserNode.isMissingNode()) {
                            bestCurrencyPair.setAdvertiserNickName(advertiserNode.path("nickName").asText());
                        }
                    }
                }
            }

            return bestCurrencyPair;

        } catch (IOException e) {
            throw new RuntimeException("Ошибка при распаковке gzip-ответа", e);
        }
    }

}



