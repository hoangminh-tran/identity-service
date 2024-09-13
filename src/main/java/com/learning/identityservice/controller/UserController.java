package com.learning.identityservice.controller;

import java.util.List;

import com.learning.identityservice.validator.UUIDConstraint;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.learning.identityservice.dto.request.ApiResponse;
import com.learning.identityservice.dto.request.UserCreationRequest;
import com.learning.identityservice.dto.request.UserUpdateRequest;
import com.learning.identityservice.dto.response.UserResponse;
import com.learning.identityservice.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    UserService userService;

    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        log.info("Controller: Create User");
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();
    }

    @GetMapping("/{userID}")
    ApiResponse<UserResponse> getUser(@PathVariable("userID") String userID) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(userID))
                .build();
    }

    @GetMapping("/my-info")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @DeleteMapping("/{userID}")
    ApiResponse<String> deleteUser(@UUIDConstraint @PathVariable String userID) {
        userService.deleteUser(userID);
        return ApiResponse.<String>builder().result("User has been deleted").build();
    }

    @PutMapping("/{userID}")
    ApiResponse<UserResponse> updateUser(@UUIDConstraint @PathVariable String userID, @Valid @RequestBody UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userID, request))
                .build();
    }
}
