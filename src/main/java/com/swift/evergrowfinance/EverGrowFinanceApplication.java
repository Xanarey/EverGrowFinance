package com.swift.evergrowfinance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableCaching
@EnableScheduling
//@EnableConfigurationProperties({JwtProperties.class, CacheProperties.class})
public class EverGrowFinanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EverGrowFinanceApplication.class, args);
    }

}
