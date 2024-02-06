package com.swift.evergrowfinance.repository;

import com.swift.evergrowfinance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
