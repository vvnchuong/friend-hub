package com.friendhub.mapper;

import com.friendhub.dto.request.AdminUserCreationRequest;
import com.friendhub.dto.request.AdminUserUpdateRequest;
import com.friendhub.dto.request.UserCreationRequest;
import com.friendhub.dto.request.UserUpdateRequest;
import com.friendhub.dto.response.AdminUserResponse;
import com.friendhub.dto.response.UserBasicInfoResponse;
import com.friendhub.dto.response.UserResponse;
import com.friendhub.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    User toUser(UserCreationRequest request);

    User toUser(AdminUserCreationRequest request);

    UserResponse toUserResponse(User user);

    UserBasicInfoResponse toUserBasicResponse(User user);

    @Mapping(target = "role", source = "role.name")
    AdminUserResponse toAdminUserResponse(User user);

    @Mapping(target = "email", ignore = true)
    @Mapping(target = "role", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role.name", source = "role")
    void updateAdminUser(@MappingTarget User user, AdminUserUpdateRequest request);

}
