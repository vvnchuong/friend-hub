package com.friendhub.controller;

import com.friendhub.dto.response.ApiResponse;
import com.friendhub.dto.request.UserCreationRequest;
import com.friendhub.dto.request.UserUpdateRequest;
import com.friendhub.dto.response.FriendResponse;
import com.friendhub.dto.response.UserResponse;
import com.friendhub.service.UserService;
import com.friendhub.service.impl.FriendServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final FriendServiceImpl friendService;

    @PostMapping
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .message("User created successfully.")
                .result(userService.createUser(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<UserResponse>> getAllUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .message("Users retrieved successfully.")
                .result(userService.getAllUsers())
                .build();
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserResponse> getUserById(@PathVariable("userId") long userId) {
        return ApiResponse.<UserResponse>builder()
                .message("User retrieved successfully.")
                .result(userService.getUserById(userId))
                .build();
    }

    @PutMapping("/{userId}")
    public ApiResponse<UserResponse> updateUser(@PathVariable("userId") long userId,
                                                @RequestBody UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .message("User updated successfully.")
                .result(userService.updateUser(userId, request))
                .build();
    }

    @DeleteMapping("/{userId}")
    public ApiResponse<Void> deleteUser(@PathVariable("userId") long userId) {
        userService.deleteUser(userId);
        return ApiResponse.<Void>builder()
                .message("User deleted successfully.")
                .build();
    }

    @GetMapping("/{userId}/friends")
    public ApiResponse<List<FriendResponse>> getAllFriendsById(@PathVariable("userId") long userId) {
        return ApiResponse.<List<FriendResponse>>builder()
                .message("Friends retrieved successfully.")
                .result(friendService.getAllFriendsById(userId))
                .build();
    }


}
