package com.swift.evergrowfinance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class EverGrowFinanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EverGrowFinanceApplication.class, args);
    }

}
