package com.swift.evergrowfinance.controller;

import com.swift.evergrowfinance.dto.UserUpdateEmailDTO;
import com.swift.evergrowfinance.model.entities.User;
import com.swift.evergrowfinance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User getUser(@PathVariable Long id) {
        return userService.getUserServById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @PutMapping
    @PreAuthorize("hasAuthority('USER')")
    public String updateEmailUser(@RequestBody UserUpdateEmailDTO request) {
        User userContextHolder = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        userService.updateEmail(userContextHolder, request);
        return request.getEmail() + " - успешно установлен";
    }
}
