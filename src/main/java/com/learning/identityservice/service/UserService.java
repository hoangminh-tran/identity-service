package com.learning.identityservice.service;

import java.util.HashSet;
import java.util.List;

import com.learning.identityservice.constant.PredefinedRole;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.learning.identityservice.dto.request.UserCreationRequest;
import com.learning.identityservice.dto.request.UserUpdateRequest;
import com.learning.identityservice.dto.response.UserResponse;
import com.learning.identityservice.entity.Role;
import com.learning.identityservice.entity.User;
import com.learning.identityservice.exception.AppException;
import com.learning.identityservice.exception.ErrorCode;
import com.learning.identityservice.mapper.UserMapper;
import com.learning.identityservice.repository.RoleRepository;
import com.learning.identityservice.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

/**
 * The type User service.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    /**
     * Create user user response.
     *
     * @param request the request
     * @return the user response
     */
    public UserResponse createUser(UserCreationRequest request) {
        log.info("Service: Create User");

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);

        user.setRoles(roles);

        user = userRepository.save(user);

        return userMapper.toUserResponse(user);
    }

    /**
     * Create user with role user response.
     *
     * @param request  the request
     * @param roleName the role name
     * @return the user response
     */
    public UserResponse createUserWithRole(UserCreationRequest request, String roleName) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(roleName).ifPresent(roles::add);

        user.setRoles(roles);

        user = userRepository.save(user);

        return userMapper.toUserResponse(user);
    }

    /**
     * Gets my info.
     *
     * @return the my info
     */
    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }

    /**
     * Update user user response.
     *
     * @param userId  the user id
     * @param request the request
     * @return the user response
     */
    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    /**
     * Delete user.
     *
     * @param userId the user id
     */
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    /**
     * Gets users.
     *
     * @return the users
     */
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        log.info("In method get Users");
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    /**
     * Gets user.
     *
     * @param id the id
     * @return the user
     */
    public UserResponse getUser(String id) {
        return userMapper.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    /**
     * Count users long.
     *
     * @return the long
     */
    public long countUsers() {
        return userRepository.count();
    }
}
