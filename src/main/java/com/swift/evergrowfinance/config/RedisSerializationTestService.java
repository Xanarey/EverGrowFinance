package com.swift.evergrowfinance.config;

import com.swift.evergrowfinance.dto.UserDetailsDTO;
import com.swift.evergrowfinance.security.UserDetailsServiceImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class RedisSerializationTestService {

    private final RedisTemplate<Object, Object> redisTemplate;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    public RedisSerializationTestService(RedisTemplate<Object, Object> redisTemplate, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.redisTemplate = redisTemplate;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    public void testUserDetailsSerialization(String userEmail) {
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(userEmail);
        UserDetailsDTO dto = new UserDetailsDTO(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());

        ValueOperations<Object, Object> valueOps = redisTemplate.opsForValue();
        String key = "test:userDetails";

        // Сериализация DTO
        valueOps.set(key, dto);

        // Десериализация DTO
        UserDetailsDTO retrievedDto = (UserDetailsDTO) valueOps.get(key);
        UserDetails retrievedUserDetails = new org.springframework.security.core.userdetails
                .User(Objects.requireNonNull(retrievedDto).getUsername(), retrievedDto.getPassword(), retrievedDto.getAuthorities());

        // Вывод для проверки
        System.out.println("Retrieved UserDetails: " + retrievedUserDetails.getUsername());
    }
}