package com.swift.evergrowfinance.service;

import com.swift.evergrowfinance.model.entities.TwoFactorCode;
import com.swift.evergrowfinance.model.entities.User;
import com.swift.evergrowfinance.repository.TwoFactorCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class OtpService {

    private final TwoFactorCodeRepository twoFactorCodeRepository;
    private final EmailService emailService;

    @Autowired
    public OtpService(EmailService emailService, TwoFactorCodeRepository twoFactorCodeRepository) {
        this.twoFactorCodeRepository = twoFactorCodeRepository;
        this.emailService = emailService;
    }

    public String generateOtp(User user) {
        String code = String.valueOf(100000 + new SecureRandom().nextInt(900000));
        TwoFactorCode twoFactorCode = new TwoFactorCode();
        twoFactorCode.setUsed(false);
        twoFactorCode.setEmail(user.getEmail());
        twoFactorCode.setCode(code);
        twoFactorCode.setExpiryTime(LocalDateTime.now().plusMinutes(10));

        twoFactorCodeRepository.save(twoFactorCode);
        return twoFactorCode.getCode();
    }

    public void sendOtp(User user, String otp) {
        emailService.sendEmail(user.getEmail(), otp);
    }

    public boolean validateOtp(User user, String otp) {
        TwoFactorCode twoFactorCode = twoFactorCodeRepository.findByEmailAndCode(user.getEmail(), otp);
        if (twoFactorCode != null && LocalDateTime.now().isBefore(twoFactorCode.getExpiryTime()) && !twoFactorCode.isUsed()) {
            twoFactorCode.setUsed(true);
            twoFactorCodeRepository.save(twoFactorCode);
            return true;
        }
        return false;
    }
}
