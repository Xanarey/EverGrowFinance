package com.swift.evergrowfinance.service.impl;

import com.swift.evergrowfinance.model.User;
import com.swift.evergrowfinance.repository.UserRepository;
import com.swift.evergrowfinance.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CacheManager cacheManager;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, CacheManager cacheManager) {
        this.userRepository = userRepository;
        this.cacheManager = cacheManager;
    }

    @Override
    public List<User> getAllUsers() {
        log.info("IN UserServiceImpl getAllUsers");
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        log.info("IN UserServiceImpl getUserByEmail {}", email);
        return userRepository.findByEmail(email);
    }

    @Override
    public User getUserById(Long id) {
        log.info("IN UserServiceImpl getById {}", id);
        return userRepository.findById(id).orElse(new User());
    }

    @CacheEvict(value = "users", key = "#user.email")
    @Transactional
    @Override
    public void update(User user) {
        log.info("IN UserServiceImpl update {}", user);

        Optional<User> optionalUser = userRepository.findById(user.getId());
        if (optionalUser.isPresent() && !optionalUser.get().getEmail().equals(user.getEmail())) {
            Objects.requireNonNull(cacheManager.getCache("users")).evict(optionalUser.get().getEmail());
        }

        userRepository.save(user);
    }

    @CacheEvict(value = "users", key = "#user.email")
    @Transactional
    @Override
    public void save(User user) {
        log.info("IN UserServiceImpl save {}", user);
        userRepository.save(user);
    }
}
