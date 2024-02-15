package com.swift.evergrowfinance.service;

import com.swift.evergrowfinance.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getUserByEmail(String email);
    User getUserById(Long id);
}
