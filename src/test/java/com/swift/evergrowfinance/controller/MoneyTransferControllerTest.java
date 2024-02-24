package com.swift.evergrowfinance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swift.evergrowfinance.dto.MoneyTransferRequest;
import com.swift.evergrowfinance.model.Role;
import com.swift.evergrowfinance.model.User;
import com.swift.evergrowfinance.model.Wallet;
import com.swift.evergrowfinance.security.JwtTokenProvider;
import com.swift.evergrowfinance.service.MoneyTransferService;
import com.swift.evergrowfinance.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = MoneyTransferController.class)
class MoneyTransferControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private MoneyTransferService moneyTransferService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@example.com");
        user.setPassword("12345");
        user.setRole(Role.USER);
        Wallet wallet = new Wallet();
        wallet.setBalance(new BigDecimal("10000.0"));
        wallet.setPhoneNumber("123456789");
        user.setWallets(List.of(wallet));

        mockMvc = MockMvcBuilders.standaloneSetup(new MoneyTransferController(moneyTransferService, userService)).build();
    }

    @Test
    @WithMockUser(username = "test@example.com", authorities = {"USER"})
    void shouldTransferMoneySuccessfully() throws Exception {
        MoneyTransferRequest request = new MoneyTransferRequest("123456789", "987654321", new BigDecimal("500.0"));

        when(userService.getUserByEmail("test@example.com")).thenReturn(Optional.ofNullable(user));
        when(userService.getUserServById(user.getId())).thenReturn(Optional.of(user));

        doNothing().when(moneyTransferService).transferMoney(user, "123456789", "987654321", new BigDecimal("500.0"));

        mockMvc.perform(post("/transfer")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.message").value("Перевод успешно выполнен"));
    }

    @Test
    @WithMockUser(username = "test@example.com", authorities = {"USER"})
    void shouldThrowExceptionWhenUserNotFound() throws Exception {
        MoneyTransferRequest request = new MoneyTransferRequest("123456789", "987654321", new BigDecimal("500.0"));

        when(userService.getUserByEmail("test@example.com")).thenReturn(Optional.empty());

        mockMvc.perform(post("/transfer")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}