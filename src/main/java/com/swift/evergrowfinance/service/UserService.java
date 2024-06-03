package com.swift.evergrowfinance.service;

import com.swift.evergrowfinance.dto.RegisterRequestDTO;
import com.swift.evergrowfinance.dto.UserUpdateEmailDTO;
import com.swift.evergrowfinance.model.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getUserByEmail(String email);
    Optional<User> getUserServById(Long id);
    void update(User user);
    void save(User user);
    void delete(User user);
    void updateEmail(User user, UserUpdateEmailDTO userUpdateEmailDTO);
    void registerNewUser(RegisterRequestDTO registerRequestDTO);
}
