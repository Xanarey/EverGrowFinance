package com.swift.evergrowfinance.controllers;

import com.swift.evergrowfinance.dto.PasswordForgotDTO;
import com.swift.evergrowfinance.dto.PasswordResetDTO;
import com.swift.evergrowfinance.service.EmailService;
import com.swift.evergrowfinance.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/password")
public class PasswordController {

    private final PasswordService passwordService;
    private final EmailService emailService;

    @Autowired
    public PasswordController(PasswordService passwordService, EmailService emailService) {
        this.passwordService = passwordService;
        this.emailService = emailService;
    }

    @PostMapping("/forgot")
    public ResponseEntity<?> processForgotPassword(@RequestBody PasswordForgotDTO passwordForgotDTO) {
        String token = passwordService.createPasswordResetToken(passwordForgotDTO.getEmail());
        if (token != null) {
            emailService.sendPasswordResetEmail(passwordForgotDTO.getEmail(), token);
            return ResponseEntity.ok("Password reset email sent");
        } else {
            return ResponseEntity.badRequest().body("Email not found");
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<?> processResetPassword(@RequestBody PasswordResetDTO passwordResetDTO) {
        boolean result = passwordService.resetPassword(passwordResetDTO.getToken(), passwordResetDTO.getPassword());
        if (result) {
            return ResponseEntity.ok("Password reset successful");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired token");
        }
    }
}
