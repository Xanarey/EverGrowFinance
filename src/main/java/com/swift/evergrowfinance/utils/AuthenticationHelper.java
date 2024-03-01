package com.swift.evergrowfinance.utils;

import com.swift.evergrowfinance.dto.AuthRequestDTO;
import com.swift.evergrowfinance.model.entities.User;
import com.swift.evergrowfinance.security.JwtTokenProvider;
import com.swift.evergrowfinance.service.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.streams.kstream.EmitStrategy.log;

public class AuthenticationHelper {
    public static Map<String, String> getMap(UserService userService, JwtTokenProvider jwtTokenProvider, AuthRequestDTO requestDto) {
        User user = userService.getUserByEmail(requestDto.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User does`t exists"));
        String token = jwtTokenProvider.createToken(requestDto.getEmail(), user.getRole().name());
        Map<String, String> response = new HashMap<>();
        log.info("IN AuthenticationRestController authenticate");
        response.put("email", requestDto.getEmail());
        response.put("token", token);
        return response;
    }
}
