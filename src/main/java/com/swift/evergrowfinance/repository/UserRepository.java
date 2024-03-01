package com.swift.evergrowfinance.repository;

import com.swift.evergrowfinance.model.entities.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserById(Long id);

    @Cacheable(value = "users", key = "#email")
    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = {"wallets"})
    @NonNull
    List<User> findAll();

    @Query("SELECT u FROM User u JOIN FETCH u.subscriptions")
    List<User> findAllBySubscriptions();
}
