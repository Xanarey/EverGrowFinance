package com.swift.evergrowfinance.service.impl;

import com.swift.evergrowfinance.model.User;
import com.swift.evergrowfinance.repository.UserRepository;
import com.swift.evergrowfinance.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        log.info("IN UserServiceImpl getAllUsers");
//        return userRepository.findAllWithWallets();
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        log.info("IN UserServiceImpl getById {}", id);
        return userRepository.findById(id).orElse(new User());
    }
}