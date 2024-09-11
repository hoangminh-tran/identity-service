package com.learning.identityservice.controller;

import com.learning.identityservice.dto.request.UserCreationRequest;
import com.learning.identityservice.dto.response.UserResponse;
import com.learning.identityservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

@Slf4j
@SpringBootTest // Initializes the Spring context, providing all beans of the application for testing
@AutoConfigureMockMvc
// Enables MockMvc, which helps simulate HTTP requests to the controller without needing to start a real server
@TestPropertySource("/test.properties")
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc; // Injects MockMvc, a tool for simulating(mô phỏng) HTTP requests to test the controller

    @MockBean
    // Creates a mock object for UserService and injects it into the Spring context. The methods of this mock will be controlled by Mockito
    private UserService userService;

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

        userResponse = UserResponse
                .builder()
                .id("0741195e")
                .username("lalalisa")
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

        Mockito.when(userService.createUser(ArgumentMatchers.any())) // Mocks the behavior of the createUser method
                .thenReturn(userResponse);  // Returns the default userResponse object

        // WHEN, THEN
        mockMvc
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
                                .jsonPath("result.id")
                                .value("0741195e")
                );
    }

    @Test
        // Marks this method as a test case
    void createUser_usernameInvalidRequest_fail() throws Exception {
        // GIVEN
        userCreationRequest.setUsername("lis");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(userCreationRequest);

        Mockito.when(userService.createUser(ArgumentMatchers.any())) // Mocks the behavior of the createUser method
                .thenReturn(userResponse);  // Returns the default userResponse object

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
