package com.friendhub.controller;

import com.friendhub.dto.response.ApiResponse;
import com.friendhub.dto.request.UserCreationRequest;
import com.friendhub.dto.request.UserUpdateRequest;
import com.friendhub.dto.response.FriendResponse;
import com.friendhub.dto.response.UserResponse;
import com.friendhub.service.AccountService;
import com.friendhub.service.FriendService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final AccountService accountService;
    private final FriendService friendService;

    @PostMapping
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .message("User created successfully.")
                .result(accountService.register(request))
                .build();
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserResponse> getUserById(@PathVariable("userId") long userId) {
        return ApiResponse.<UserResponse>builder()
                .message("User retrieved successfully.")
                .result(accountService.getUserById(userId))
                .build();
    }

    @PutMapping("/{userId}")
    public ApiResponse<UserResponse> updateMyProfile(@RequestBody @Valid UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .message("User updated successfully.")
                .result(accountService.updateMyProfile(request))
                .build();
    }


}
