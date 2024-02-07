package com.swift.evergrowfinance.service;

import com.swift.evergrowfinance.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();
    User getUserById(Long id);
    void updateUser(User user);

}
