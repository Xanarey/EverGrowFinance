package com.swift.evergrowfinance.repository;

import com.swift.evergrowfinance.model.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Cacheable("users")
    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = {"wallets"})
    @NonNull
    List<User> findAll();
}
