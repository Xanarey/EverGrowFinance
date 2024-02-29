package com.swift.evergrowfinance.security;

import com.swift.evergrowfinance.model.entities.User;
import com.swift.evergrowfinance.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsServiceImpl")
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("IN UserDetailsServiceImpl loadUserByUsername");
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User does`t exists "));
        return SecurityUser.fromUser(user);
    }

}
