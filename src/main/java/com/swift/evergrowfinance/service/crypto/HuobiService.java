package com.swift.evergrowfinance.service.crypto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swift.evergrowfinance.dto.crypto.CurrencyPairDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Iterator;

@Service
public class HuobiService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final int SELL = 1;

    private static final String HTX_URL = "https://www.huobi.eu.com/-/x/otc/v1/data/trade-market?coinId=1&currency=2&tradeType=sell&currPage=1&payMethod=0&acceptOrder=0&country=&blockType=general&online=1&range=0&amount=&onlyTradable=false&isFollowed=false";

    public HuobiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public CurrencyPairDTO findBestAdvertisement(double desiredAmount) {

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(HTX_URL, String.class);
        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Ошибка запроса: " + responseEntity.getStatusCode());
        }
        String json = responseEntity.getBody();
        System.out.println("Полученный JSON: " + json);

        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode dataArray = root.path("data");
            if (!dataArray.isArray()) {
                throw new RuntimeException("Поле 'data' отсутствует или не является массивом.");
            }

            CurrencyPairDTO bestAdvertisement = null;
            double bestPrice = Double.MAX_VALUE;


            Iterator<JsonNode> iterator = dataArray.elements();
            while (iterator.hasNext()) {
                JsonNode item = iterator.next();


                int tradeType = item.path("tradeType").asInt();
                if (tradeType != SELL) {
                    continue;
                }

                String minTradeLimitStr = item.path("minTradeLimit").asText();
                String maxTradeLimitStr = item.path("maxTradeLimit").asText();
                double minLimit = Double.parseDouble(minTradeLimitStr);
                double maxLimit = Double.parseDouble(maxTradeLimitStr);

                if (desiredAmount < minLimit || desiredAmount > maxLimit) {
                    continue;
                }

                String priceStr = item.path("price").asText();
                double price = Double.parseDouble(priceStr);

                if (price < bestPrice) {
                    bestPrice = price;
                    bestAdvertisement = new CurrencyPairDTO();
                    bestAdvertisement.setAdvNo(item.path("id").asText());
                    bestAdvertisement.setPrice(price);
                    bestAdvertisement.setMinAmount(minLimit);
                    bestAdvertisement.setMaxAmount(maxLimit);
                    bestAdvertisement.setAsset("BTC");
                    bestAdvertisement.setFiatUnit("USD");
                    bestAdvertisement.setAdvertiserNickName(item.path("userName").asText());
                }
            }

            return bestAdvertisement;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при обработке JSON", e);
        }
    }
}