package com.swift.evergrowfinance.service.impl;

import com.swift.evergrowfinance.model.User;
import com.swift.evergrowfinance.repository.UserRepository;
import com.swift.evergrowfinance.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
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
        userRepository.save(user);
        //TODO Проверить изменение email,нужно убедиться, что кэш инвалидируется соответствующим образом
    }

    @Override
    public void save(User user) {
        log.info("IN UserServiceImpl save {}", user);
        userRepository.save(user);
    }
}
