package com.swift.evergrowfinance.controller;

import com.swift.evergrowfinance.config.RedisSerializationTestService;
import com.swift.evergrowfinance.dto.AuthRequestDto;
import com.swift.evergrowfinance.model.User;
import com.swift.evergrowfinance.security.JwtTokenProvider;
import com.swift.evergrowfinance.security.UserDetailsServiceImpl;
import com.swift.evergrowfinance.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class AuthenticationRestController {

    private final RedisSerializationTestService redisTestService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthenticationRestController(RedisSerializationTestService redisTestService, AuthenticationManager authenticationManager, UserService userService, JwtTokenProvider jwtTokenProvider, UserDetailsServiceImpl userDetailsService) {
        this.redisTestService = redisTestService;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/auth")
    public ResponseEntity<?> authenticate(@RequestBody AuthRequestDto requestDto) {
        try {
            redisTestService.testUserDetailsSerialization(requestDto.getEmail());

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword()));
            User user = userService.getUserByEmail(requestDto.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User does`t exists"));

            String token = jwtTokenProvider.createToken(requestDto.getEmail(), user.getRole().name());
            Map<Object, Object> response = new HashMap<>();
            log.info("IN AuthenticationRestController authenticate");
            response.put("email", requestDto.getEmail());
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Invalid email/password combination", HttpStatus.UNAUTHORIZED);
        }
    }
}
