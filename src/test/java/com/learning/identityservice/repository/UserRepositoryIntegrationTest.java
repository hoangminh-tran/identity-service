package com.learning.identityservice.repository;

import com.learning.identityservice.constant.PredefinedRole;
import com.learning.identityservice.entity.Role;
import com.learning.identityservice.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.HashSet;

@SpringBootTest
@Testcontainers
@Slf4j
@AutoConfigureMockMvc
public class UserRepositoryIntegrationTest {
    @Container
    static final MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer<>("mysql:latest");

    @DynamicPropertySource
    static void configureDataSource(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
        dynamicPropertyRegistry.add("spring.datasource..driver-class-name", MY_SQL_CONTAINER::getDriverClassName);
        dynamicPropertyRegistry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @Autowired
    UserRepository userRepository;

    @MockBean
    RoleRepository roleRepository;

    private User user;

    @BeforeEach
        // Runs before each test case, used to initialize or set up the necessary data for the tests
    void initializeData() {
        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);

        user = User
                .builder()
                .id("0741195e-1774-4cd5-a47c-e99db62d5aa0")
                .username("phamthanhgiang458")
                .password("Aa@123456")
                .firstName("Pham")
                .lastName("Thanh Giang")
                .dob(LocalDate.of(1995, 8, 20))
                .roles(roles)
                .build();
    }

    @Test
    void connectionEstablished() {
        Assertions.assertEquals(MY_SQL_CONTAINER.isCreated(), true);
        Assertions.assertEquals(MY_SQL_CONTAINER.isRunning(), true);
        Assertions.assertEquals(userRepository.findAll().size(), 10);
    }

    @Test
    void existsByUsername_userNameIsExisted_success() {
        var userName = "phamthanhgiang458";

        Assertions.assertEquals(userRepository.existsByUsername(userName), true);
    }

    @Test
    void existsByUsername_userNameNotExisted_fail() {
        var userName = "phamthanhgiang458999";

        Assertions.assertEquals(userRepository.existsByUsername(userName), false);
    }

    @Test
    void findByUsername_userNameIsExisted_success() {
        var userName = "phamthanhgiang458";
        var userResponse = userRepository.findByUsername(userName).get();

        Assertions.assertEquals(user.getUsername(), userResponse.getUsername());
        Assertions.assertEquals(user.getFirstName(), userResponse.getFirstName());
        Assertions.assertEquals(user.getLastName(), userResponse.getLastName());
        Assertions.assertEquals(user.getDob(), userResponse.getDob());
    }

    @Test
    void findByUsername_userNameNotExisted_fail() {
        var userName = "phamthanhgiang45899";
        var userResponse = userRepository.findByUsername(userName).orElse(null);

        Assertions.assertNull(userResponse);
    }
}
