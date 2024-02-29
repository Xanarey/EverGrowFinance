package com.swift.evergrowfinance.repository;

import com.swift.evergrowfinance.model.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByPhoneNumber(String phoneNumber);
}
