package com.swift.evergrowfinance.controllers;

import com.swift.evergrowfinance.dto.PasswordForgotDTO;
import com.swift.evergrowfinance.dto.PasswordResetDTO;
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

    @Autowired
    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @PostMapping("/forgot")
    public ResponseEntity<?> processForgotPassword(@RequestBody PasswordForgotDTO passwordForgotDTO) {
        return passwordService.getStringResponseEntity(passwordForgotDTO);
    }

    @PostMapping("/reset")
    public ResponseEntity<?> processResetPassword(@RequestBody PasswordResetDTO passwordResetDTO) {
        return passwordService.getStringResetPasswordEntity(passwordResetDTO);
    }


}
