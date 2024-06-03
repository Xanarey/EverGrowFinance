package com.swift.evergrowfinance.service.impl;

import com.swift.evergrowfinance.dto.RegisterRequestDTO;
import com.swift.evergrowfinance.dto.UserUpdateEmailDTO;
import com.swift.evergrowfinance.model.entities.Subscription;
import com.swift.evergrowfinance.model.entities.Transaction;
import com.swift.evergrowfinance.model.entities.User;
import com.swift.evergrowfinance.model.entities.Wallet;
import com.swift.evergrowfinance.model.enums.Role;
import com.swift.evergrowfinance.repository.UserRepository;
import com.swift.evergrowfinance.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
//    private final CacheManager cacheManager;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAllUsers() {
        log.info("IN UserServiceImpl getAllUsers");
        return userRepository.findAll();
    }

    @Cacheable(value = "users", key = "#email", unless = "#result == null")
    @Override
    public Optional<User> getUserByEmail(String email) {
        log.info("IN UserServiceImpl getUserByEmail {}", email);
        return userRepository.findByEmail(email);
    }

    @Cacheable(value = "users", key = "#id", unless = "#result == null")
    @Override
    public Optional<User> getUserServById(Long id) {
        log.info("IN UserServiceImpl getUserById {}", id);
        return userRepository.findUserById(id);
    }

    @CachePut(value = "users", key = "#user.id")
    @Override
    public void update(User user) {
        log.info("IN UserServiceImpl update {}", user);
        userRepository.save(user);
    }

    @Transactional
    @CachePut(value = "users", key = "#user.id")
    @Override
    public void save(User user) {
        log.info("IN UserServiceImpl save {}", user);
        userRepository.save(user);
    }

    @CacheEvict(value = "users", key = "#user.id")
    @Override
    public void delete(User user) {
        log.info("IN UserServiceImpl delete {}", user);
        userRepository.delete(user);
    }

    @Transactional
    @Override
    public void updateEmail(User user, UserUpdateEmailDTO userUpdateEmailDTO) {
//        String oldEmail = user.getEmail();
        if (userRepository.findByEmail(userUpdateEmailDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email уже используется");
        }

        user.setEmail(userUpdateEmailDTO.getEmail());

//        User updatedUser = update(user);
//        Objects.requireNonNull(cacheManager.getCache("users")).evict(oldEmail);
//        Objects.requireNonNull(cacheManager.getCache("users")).put(updatedUser.getEmail(), updatedUser);

        log.info("IN UserServiceImpl updateEmail {}", user);
    }

    @Transactional
    @Override
    public void registerNewUser(RegisterRequestDTO registerRequestDTO) {
        User user = new User();
        user.setEmail(registerRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        user.setRole(Role.USER);

        List<Wallet> wallets = new ArrayList<>();
        List<Subscription> subscriptions = new ArrayList<>();
        List<Transaction> transactions = new ArrayList<>();

        Wallet wallet = new Wallet();
        wallet.setPhoneNumber(registerRequestDTO.getPhoneNumber());
        wallet.setBalance(new BigDecimal(0));
        wallet.setCreatedAt(LocalDateTime.now());
        wallet.setWalletType(registerRequestDTO.getWalletType());
        wallet.setUser(user);

        wallets.add(wallet);

        user.setWallets(wallets);
        user.setSubscriptions(subscriptions);
        user.setTransactions(transactions);
        log.info("IN UserServiceImpl registerNewUser {}", registerRequestDTO);
        userRepository.save(user);
    }

}
