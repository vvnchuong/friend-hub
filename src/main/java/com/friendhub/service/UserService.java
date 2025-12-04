package com.friendhub.service;

import com.friendhub.dto.request.UserCreationRequest;
import com.friendhub.dto.request.UserUpdateRequest;
import com.friendhub.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse createUser(UserCreationRequest request);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(long userId);

    UserResponse updateUser(long id, UserUpdateRequest request);

    void deleteUser(long userId);

}
