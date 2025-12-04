package com.friendhub.mapper;

import com.friendhub.dto.request.UserCreationRequest;
import com.friendhub.dto.request.UserUpdateRequest;
import com.friendhub.dto.response.UserResponse;
import com.friendhub.entity.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserCreationRequest request);

    @Mapping(target = "role", source = "role.name")
    UserResponse toUserResponse(User user);

    @Mapping(target = "email", ignore = true)
    @Mapping(target = "role", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

    @AfterMapping
    default void setCreatedAt(@MappingTarget User user) {
        if (user.getCreatedAt() == null)
            user.setCreatedAt(Instant.now());
    }

    @AfterMapping
    default void setUpdatedAt(@MappingTarget User user) {
        user.setUpdatedAt(Instant.now());
    }

}
