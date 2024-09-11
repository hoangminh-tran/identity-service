package com.learning.identityservice.mapper;

import com.learning.identityservice.entity.Role;
import org.mapstruct.Mapper;

import com.learning.identityservice.dto.request.RoleRequest;
import com.learning.identityservice.dto.response.RoleResponse;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
