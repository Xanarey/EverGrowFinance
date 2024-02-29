package com.swift.evergrowfinance.controller;

import com.swift.evergrowfinance.dto.AuthRequestDto;
import com.swift.evergrowfinance.model.entities.User;
import com.swift.evergrowfinance.security.JwtTokenProvider;
import com.swift.evergrowfinance.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class AuthenticationRestController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthenticationRestController(AuthenticationManager authenticationManager, UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    
    @PostMapping("/auth")
    public Map<String, String> authenticate(@RequestBody AuthRequestDto requestDto) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword()));
            return getMap(requestDto);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email/password combination");
        }
    }

    private Map<String, String> getMap(AuthRequestDto requestDto) {
        User user = userService.getUserByEmail(requestDto.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User does`t exists"));
        String token = jwtTokenProvider.createToken(requestDto.getEmail(), user.getRole().name());
        Map<String, String> response = new HashMap<>();
        log.info("IN AuthenticationRestController authenticate");
        response.put("email", requestDto.getEmail());
        response.put("token", token);
        return response;
    }
}
