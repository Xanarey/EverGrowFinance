//package com.swift.evergrowfinance.repository;
//
//import com.swift.evergrowfinance.model.enums.Role;
//import com.swift.evergrowfinance.model.entities.Subscription;
//import com.swift.evergrowfinance.model.entities.User;
//import com.swift.evergrowfinance.model.entities.Wallet;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@ExtendWith(SpringExtension.class)
//@DataJpaTest
//@AutoConfigureTestDatabase
//public class UserRepositoryTest {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private TestEntityManager entityManager;
//
//    @BeforeEach
//    public void setUp() {
//        User user1 = new User();
//        user1.setEmail("$2a$12$rXigBnouGo59Z24AN9sol.foli24nY.aPHBKZCIWh/kNchDGF52lm");
//        user1.setPassword("$2a$15$pimrUrY9qsbkDVDhoWzBxOEUb0OWz5Oe0ETVf8txxH1fJ1CqRmd.O");
//        user1.setRole(Role.USER);
//        user1.setWallets(List.of(new Wallet()));
//
//        Subscription subscription1 = new Subscription();
//        subscription1.setName("BxOEUb0OWz5Oe0ET");
//        subscription1.setPrice(new BigDecimal("922.00"));
//        subscription1.setStartDate(LocalDateTime.now());
//        subscription1.setEndDate(LocalDateTime.now());
//        subscription1.setStatus("ACTIVE");
//        subscription1.setType("premium");
//        subscription1.setPaymentFrequency("monthly");
//        subscription1.setAuto_renew(true);
//        subscription1.setWalletNumber("89999999999");
//        subscription1.setUser(user1);
//        user1.setSubscriptions(List.of(subscription1));
//
//        entityManager.persist(user1);
//
//        User user2 = new User();
//        user2.setEmail("$2a$12$rXigBnouGo59Z24AN9so9sol.foli24nY.aPHBKZCIWh/k");
//        user2.setPassword("pimrUrY9qsbkDVDhoWzBxOEUb");
//        user2.setRole(Role.USER);
//        user2.setWallets(List.of(new Wallet()));
//
//        Subscription subscription2 = new Subscription();
//        subscription2.setName("DVDhoWzBxOEigBnouGo59Z2Ub(AS");
//        subscription2.setPrice(new BigDecimal("2922.00"));
//        subscription2.setStartDate(LocalDateTime.now());
//        subscription2.setEndDate(LocalDateTime.now());
//        subscription2.setStatus("ACTIVE");
//        subscription2.setType("premium");
//        subscription2.setPaymentFrequency("monthly");
//        subscription2.setAuto_renew(true);
//        subscription2.setWalletNumber("89999999992");
//        subscription2.setUser(user2);
//        user2.setSubscriptions(List.of(subscription2));
//
//        entityManager.persist(user2);
//    }
//
//    @Test
//    public void testFindAllBySubscriptions() {
//        List<User> users = userRepository.findAllBySubscriptions();
//        assertThat(users).isNotEmpty();
//        assertThat(users.size()).isEqualTo(2);
//
//        User firstUser = users.get(0);
//        assertThat(firstUser.getEmail()).isEqualTo("$2a$12$rXigBnouGo59Z24AN9sol.foli24nY.aPHBKZCIWh/kNchDGF52lm");
//        assertThat(firstUser.getPassword()).isEqualTo("$2a$15$pimrUrY9qsbkDVDhoWzBxOEUb0OWz5Oe0ETVf8txxH1fJ1CqRmd.O");
//        assertThat(firstUser.getRole()).isEqualTo(Role.USER);
//        assertThat(firstUser.getWallets()).isNotEmpty();
//        assertThat(firstUser.getSubscriptions()).isNotEmpty();
//
//        Subscription firstUserSubscription = firstUser.getSubscriptions().get(0);
//        assertThat(firstUserSubscription.getName()).isEqualTo("BxOEUb0OWz5Oe0ET");
//        assertThat(firstUserSubscription.getPrice()).isEqualByComparingTo(new BigDecimal("922.00"));
//        assertThat(firstUserSubscription.getStartDate()).isEqualToIgnoringHours(LocalDateTime.now());
//        assertThat(firstUserSubscription.getEndDate()).isEqualToIgnoringHours(LocalDateTime.now());
//        assertThat(firstUserSubscription.getStatus()).isEqualTo("ACTIVE");
//        assertThat(firstUserSubscription.getType()).isEqualTo("premium");
//        assertThat(firstUserSubscription.getAuto_renew()).isTrue();
//        assertThat(firstUserSubscription.getWalletNumber()).isEqualTo("89999999999");
//
//        User secondUser = users.get(1);
//        assertThat(secondUser.getEmail()).isEqualTo("$2a$12$rXigBnouGo59Z24AN9so9sol.foli24nY.aPHBKZCIWh/k");
//        assertThat(secondUser.getPassword()).isEqualTo("pimrUrY9qsbkDVDhoWzBxOEUb");
//        assertThat(secondUser.getRole()).isEqualTo(Role.USER);
//        assertThat(secondUser.getWallets()).isNotEmpty();
//        assertThat(secondUser.getSubscriptions()).isNotEmpty();
//
//        Subscription secondUserSubscription = secondUser.getSubscriptions().get(0);
//        assertThat(secondUserSubscription.getName()).isEqualTo("DVDhoWzBxOEigBnouGo59Z2Ub(AS");
//        assertThat(secondUserSubscription.getPrice()).isEqualByComparingTo(new BigDecimal("2922.00"));
//        assertThat(secondUserSubscription.getStartDate()).isEqualToIgnoringHours(LocalDateTime.now());
//        assertThat(secondUserSubscription.getEndDate()).isEqualToIgnoringHours(LocalDateTime.now());
//        assertThat(secondUserSubscription.getStatus()).isEqualTo("ACTIVE");
//        assertThat(secondUserSubscription.getType()).isEqualTo("premium");
//        assertThat(secondUserSubscription.getAuto_renew()).isTrue();
//        assertThat(secondUserSubscription.getWalletNumber()).isEqualTo("89999999992");
//    }
//
//    @AfterEach
//    public void tearDown() {
//        entityManager.clear();
//    }
//}
//
