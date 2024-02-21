package com.swift.evergrowfinance.service;

import com.swift.evergrowfinance.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getUserByEmail(String email);
    Optional<User> getUserServById(Long id);
    User update(User user);
    User save(User user);
    void delete(User user);
}
