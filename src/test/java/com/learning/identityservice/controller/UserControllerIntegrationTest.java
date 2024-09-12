package com.learning.identityservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.learning.identityservice.dto.request.UserCreationRequest;
import com.learning.identityservice.dto.response.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

@Slf4j
@SpringBootTest // Initializes the Spring context, providing all beans of the application for testing
@AutoConfigureMockMvc
// Enables MockMvc, which helps simulate HTTP requests to the controller without needing to start a real server
@Testcontainers
// Annotation that enables the usage of Testcontainers, which are used to spin up Docker containers for testing
public class UserControllerIntegrationTest {
    @Container
    static final MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer<>("mysql:latest");
    // @Container: Marks a Testcontainer-managed Docker container.
    // MySQLContainer: Provides a containerized version of a MySQL database, used to simulate a real database for the tests.

    @DynamicPropertySource
    static void configureDataSource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "com.mysql.cj.jdbc.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }
    // @DynamicPropertySource: Dynamically sets properties for the application context, such as database URL, username, password, and other configurations.
    // This ensures that the Spring application will use the containerized MySQL database for testing.

    @Autowired
    private MockMvc mockMvc; // Injects MockMvc, a tool for simulating(mô phỏng) HTTP requests to test the controller

    private UserCreationRequest userCreationRequest;

    private UserResponse userResponse;

    @BeforeEach
        // Runs before each test case, used to initialize or set up the necessary data for the tests
    void initializeData() {
        userCreationRequest = UserCreationRequest
                .builder()
                .username("lalalisa")
                .password("Aa@123456")
                .firstName("la la")
                .lastName("lisa")
                .dob(LocalDate.of(1997, 3, 27))
                .build();
    }

    @Test
        // Marks this method as a test case
    void createUser_validRequest_success() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(userCreationRequest);

        // WHEN, THEN
        var response = mockMvc
                .perform(
                        MockMvcRequestBuilders // Creates and configures HTTP requests for testing.
                                .post("/users")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(content))
                .andExpect(
                        MockMvcResultMatchers // Asserts and verifies the results of HTTP requests.
                                .status()
                                .isOk()
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath("code")
                                .value(1000)
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath("result.username")
                                .value("lalalisa")
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath("result.firstName")
                                .value("la la")
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath("result.lastName")
                                .value("lisa")
                );
        log.info("Result: {}", response.andReturn().getResponse().getContentAsString());
    }

    @Test
        // Marks this method as a test case
    void createUser_usernameInvalidRequest_fail() throws Exception {
        // GIVEN
        userCreationRequest.setUsername("lis");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(userCreationRequest);

        // WHEN, THEN
        mockMvc
                .perform(
                        MockMvcRequestBuilders  // Creates and configures HTTP requests for testing.
                                .post("/users")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(content))
                .andExpect(
                        MockMvcResultMatchers // Asserts and verifies the results of HTTP requests.
                                .status()
                                .isBadRequest()
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath("code")
                                .value(1003)
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath("message")
                                .value("Username must be at least 4 characters")
                );
    }
}
