package com.learning.identityservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.learning.identityservice.dto.request.RoleRequest;
import com.learning.identityservice.dto.response.RoleResponse;
import com.learning.identityservice.mapper.RoleMapper;
import com.learning.identityservice.repository.RoleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

/**
 * The type Role service.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;

    /**
     * Create role response.
     *
     * @param request the request
     * @return the role response
     */
    public RoleResponse create(RoleRequest request) {
        var role = roleMapper.toRole(request);
        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    /**
     * Gets all.
     *
     * @return the all
     */
    public List<RoleResponse> getAll() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }

    /**
     * Delete.
     *
     * @param role the role
     */
    public void delete(String role) {
        roleRepository.deleteById(role);
    }

    /**
     * Count roles long.
     *
     * @return the long
     */
    public long countRoles() {
        return roleRepository.count();
    }
}
