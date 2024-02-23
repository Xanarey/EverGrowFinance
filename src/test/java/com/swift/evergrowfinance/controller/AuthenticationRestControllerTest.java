package com.swift.evergrowfinance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swift.evergrowfinance.dto.AuthRequestDto;
import com.swift.evergrowfinance.model.Role;
import com.swift.evergrowfinance.model.User;
import com.swift.evergrowfinance.security.JwtTokenProvider;
import com.swift.evergrowfinance.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AuthenticationRestController.class)
class AuthenticationRestControllerTest {

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AuthenticationRestController(authenticationManager, userService, jwtTokenProvider)).build();
    }

    @Test
    void shouldAuthenticateUser() throws Exception {
        AuthRequestDto requestDto = new AuthRequestDto("user@example.com", "password");
        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword("12345");
        user.setRole(Role.USER);

        when(passwordEncoder.encode("12345")).thenReturn("encodedPassword");
        when(authenticationManager.authenticate(any())).thenReturn(mock(Authentication.class));
        when(userService.getUserByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(jwtTokenProvider.createToken("user@example.com", user.getRole().name())).thenReturn("token");

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@example.com"))
                .andExpect(jsonPath("$.token").value("token"));
    }

    @Test
    void shouldReturnUnauthorizedForInvalidUser() throws Exception {
        AuthRequestDto requestDto = new AuthRequestDto("invalid@example.com", "password");

        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Invalid email/password combination"));
        when(userService.getUserByEmail("invalid@example.com")).thenReturn(Optional.empty());

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isUnauthorized());
    }
}
