package com.learning.identityservice.service;

import com.learning.identityservice.dto.request.UserCreationRequest;
import com.learning.identityservice.dto.response.UserResponse;
import com.learning.identityservice.entity.User;
import com.learning.identityservice.exception.AppException;
import com.learning.identityservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
@Slf4j
@TestPropertySource("/test.properties")
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private UserCreationRequest userCreationRequest;

    private UserResponse userResponse;

    private User user;

    @BeforeEach
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

        user = User
                .builder()
                .id("0741195e")
                .username("lalalisa")
                .firstName("la la")
                .lastName("lisa")
                .build();
    }

    @Test
    void createUser_validRequest_success() {
        // GIVEN
        Mockito.when(userRepository.existsByUsername(anyString())).thenReturn(false);
        Mockito.when(userRepository.save(any())).thenReturn(user);

        // WHEN
        var response = userService.createUser(userCreationRequest);

        // THEN
        Assertions.assertEquals(response.getId(), "0741195e");
        Assertions.assertEquals(response.getUsername(), "lalalisa");
        Assertions.assertEquals(response.getFirstName(), "la la");
        Assertions.assertEquals(response.getLastName(), "lisa");
    }

    @Test
    void createUser_userExisted_fail() {
        // GIVEN
        Mockito.when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // WHEN
        var exception = Assertions.assertThrows(AppException.class, () -> userService.createUser(userCreationRequest));
        Assertions.assertEquals(exception.getErrorCode().getCode(), 1002);
    }

    @Test
    @WithMockUser(username = "lalalisa")
    void getMyInfo_valid_success() {
        // GIVEN
        Mockito.when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        // WHEN
        var response = userService.getMyInfo();

        Assertions.assertEquals(response.getId(), "0741195e");
        Assertions.assertEquals(response.getUsername(), "lalalisa");
        Assertions.assertEquals(response.getFirstName(), "la la");
        Assertions.assertEquals(response.getLastName(), "lisa");
    }

    @Test
    @WithMockUser(username = "lalalisa")
    void getMyInfo_userNotFound_fail() {
        // GIVEN
        Mockito.when(userRepository.findByUsername(anyString())).thenReturn(Optional.ofNullable(null));

        // WHEN
        var exception = Assertions.assertThrows(AppException.class, () -> userService.getMyInfo());

        Assertions.assertEquals(exception.getErrorCode().getCode(), 1005);
    }
}
