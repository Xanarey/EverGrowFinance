package com.swift.evergrowfinance.repository;

import com.swift.evergrowfinance.model.entities.TwoFactorCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TwoFactorCodeRepository extends JpaRepository<TwoFactorCode, Long> {
    TwoFactorCode findByEmailAndCode(String email, String code);
}
