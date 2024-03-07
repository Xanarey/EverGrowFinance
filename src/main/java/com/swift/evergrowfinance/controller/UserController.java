package com.swift.evergrowfinance.controller;

import com.swift.evergrowfinance.dto.PasswordChangeDTO;
import com.swift.evergrowfinance.dto.UserUpdateEmailDTO;
import com.swift.evergrowfinance.model.entities.User;
import com.swift.evergrowfinance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User getUser(@PathVariable Long id) {
        return userService.getUserServById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
    @GetMapping("/me")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public User getMyInfo() {
        return userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
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

    @PutMapping("/change-password")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public String changeUserPassword(@RequestBody PasswordChangeDTO request) {
        User user = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return "Неверный старый пароль";
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return "Пароли не совпадают";
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userService.update(user);
        return "Пароль успешно изменен";
    }

}
