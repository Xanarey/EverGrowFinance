package com.swift.evergrowfinance.service;

import com.swift.evergrowfinance.model.User;
import com.swift.evergrowfinance.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class SubscriptionCheckService {

    private final UserRepository userRepository;

    @Autowired
    public SubscriptionCheckService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    @Scheduled(fixedRate = 10000)
    @Transactional
    public void checkSubscriptions() {
        List<User> users = userRepository.findAllBySubscriptions();

        users.forEach(user -> user.getSubscriptions().forEach(subscription -> {
            LocalDateTime now = LocalDateTime.now();
            if (subscription.getEndDate().isAfter(now)) {
                Duration duration = Duration.between(now, subscription.getEndDate());
                long days = duration.toDays();
                long hours = duration.toHours() % 24;
                long minutes = duration.toMinutes() % 60;
                log.info("Подписка пользователя " + user.getEmail() + " на " + subscription.getName() + " действует еще "
                        + days + " дней " + hours + " часов " + minutes + " минут.");
            }
        }));
    }
}
