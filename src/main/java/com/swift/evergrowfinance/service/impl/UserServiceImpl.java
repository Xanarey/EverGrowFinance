package com.swift.evergrowfinance.service.impl;

import com.swift.evergrowfinance.model.User;
import com.swift.evergrowfinance.repository.UserRepository;
import com.swift.evergrowfinance.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        return userRepository.findAll();
    }

    @Cacheable(value = "users", key = "#email")
    @Override
    public Optional<User> getUserByEmail(String email) {
        log.info("IN UserServiceImpl getUserByEmail {}", email);
        return userRepository.findByEmail(email);
    }

    @Cacheable(value = "users", key = "#id")
    @Override
    public Optional<User> getUserServById(Long id) {
        log.info("IN UserServiceImpl getUserById {}", id);
        return userRepository.findUserById(id);
    }

    @CachePut(value = "users", key = "#user.id")
    @Transactional
    @Override
    public void update(User user) {
        log.info("IN UserServiceImpl update {}", user);
        userRepository.save(user);
    }

    @CachePut(value = "users", key = "#user.id")
    @Transactional
    @Override
    public void save(User user) {
        log.info("IN UserServiceImpl save {}", user);
        userRepository.save(user);
    }

    @CacheEvict(value = "users", key = "#user.id")
    @Transactional
    @Override
    public void delete(User user) {
        log.info("IN UserServiceImpl delete {}", user);
        userRepository.delete(user);
    }
}
