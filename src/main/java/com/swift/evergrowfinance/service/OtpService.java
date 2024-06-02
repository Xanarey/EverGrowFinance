package com.swift.evergrowfinance.service;

import com.swift.evergrowfinance.model.entities.User;
import org.springframework.stereotype.Service;

@Service
public class OtpService {

    public String generateOtp(User user) {

        return "123456";
    }

    public void sendOtp(User user, String otp) {

    }

    public boolean validateOtp(User user, String otp) {

        return "123456".equals(otp);
    }
}
