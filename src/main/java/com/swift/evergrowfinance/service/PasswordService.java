package com.swift.evergrowfinance.service;

import com.swift.evergrowfinance.dto.PasswordForgotDTO;
import com.swift.evergrowfinance.dto.PasswordResetDTO;
import com.swift.evergrowfinance.model.entities.PasswordResetToken;
import com.swift.evergrowfinance.model.entities.User;
import com.swift.evergrowfinance.repository.PasswordResetTokenRepository;
import com.swift.evergrowfinance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Autowired
    public PasswordService(UserRepository userRepository, PasswordResetTokenRepository tokenRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public ResponseEntity<String> getStringResponseEntity(PasswordForgotDTO passwordForgotDTO) {
        String token = createPasswordResetToken(passwordForgotDTO.getEmail());
        if (token != null) {
            emailService.sendPasswordResetEmail(passwordForgotDTO.getEmail(), token);
            return ResponseEntity.ok("Password reset email sent");
        } else {
            return ResponseEntity.badRequest().body("Email not found");
        }
    }

    public ResponseEntity<String> getStringResetPasswordEntity(PasswordResetDTO passwordResetDTO) {
        boolean result = resetPassword(passwordResetDTO.getToken(), passwordResetDTO.getPassword());
        if (result) {
            return ResponseEntity.ok("Password reset successful");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired token");
        }
    }

    public String createPasswordResetToken(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String token = UUID.randomUUID().toString();
            LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(60); // Токен действителен 60 минут
            PasswordResetToken passwordResetToken = new PasswordResetToken(token, user, expiryDate);
            tokenRepository.save(passwordResetToken);
            return token;
        }
        return null;
    }

    public boolean resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token);
        if (resetToken != null && !resetToken.isExpired()) {
            User user = resetToken.getUser();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            tokenRepository.delete(resetToken);
            return true;
        }
        return false;
    }
}
