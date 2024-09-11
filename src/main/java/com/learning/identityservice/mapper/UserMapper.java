package com.learning.identityservice.mapper;

import com.learning.identityservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.learning.identityservice.dto.request.UserCreationRequest;
import com.learning.identityservice.dto.request.UserUpdateRequest;
import com.learning.identityservice.dto.response.UserResponse;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
