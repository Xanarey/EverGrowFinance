package com.swift.evergrowfinance.service.impl;

import com.swift.evergrowfinance.model.enums.Role;
import com.swift.evergrowfinance.model.entities.Subscription;
import com.swift.evergrowfinance.model.entities.Wallet;
import com.swift.evergrowfinance.model.entities.User;
import com.swift.evergrowfinance.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@gmail.com");
        user.setPassword("$2a$12$foli24nY.aPHBKZCIWh/kNchDGF52lmrXigBnouGo59Z24AN9sol.");
        user.setRole(Role.ADMIN);
        user.setWallets(List.of(new Wallet()));
        user.setSubscriptions(List.of(new Subscription()));
    }

    @Test
    void getAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        assertThat(userService.getAllUsers()).isEqualTo(List.of(user));
        verify(userRepository).findAll();
    }

    @Test
    void getUserByEmail() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.ofNullable(user));
        assertThat(userService.getUserByEmail(user.getEmail())).isEqualTo(Optional.ofNullable(user));
        verify(userRepository).findByEmail(user.getEmail());
    }

    @Test
    void getUserServById() {
        when(userRepository.findUserById(user.getId())).thenReturn(Optional.ofNullable(user));
        assertThat(userService.getUserServById(user.getId())).isEqualTo(Optional.ofNullable(user));
        verify(userRepository).findUserById(user.getId());
    }

    @Test
    void update() {
        when(userRepository.save(user)).thenReturn(user);
        assertThat(userService.update(user)).isEqualTo(user);
        verify(userRepository).save(user);
    }

    @Test
    void save() {
        when(userRepository.save(user)).thenReturn(user);
        assertThat(userService.save(user)).isEqualTo(user);
        verify(userRepository).save(user);
    }

    @Test
    void delete() {
        userService.delete(user);
        verify(userRepository).delete(user);
    }
}








