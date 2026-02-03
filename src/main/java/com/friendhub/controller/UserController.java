package com.friendhub.controller;

import com.friendhub.dto.request.UserUpdateStatusRequest;
import com.friendhub.dto.request.ChangePasswordRequest;
import com.friendhub.dto.response.ApiResponse;
import com.friendhub.dto.request.UserUpdateRequest;
import com.friendhub.dto.response.UserResponse;
import com.friendhub.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final AccountService accountService;

    @GetMapping("/{userId}")
    public ApiResponse<UserResponse> getUserById(
            @PathVariable("userId") long userId) {
        return ApiResponse.<UserResponse>builder()
                .message("User retrieved successfully.")
                .result(accountService.getUserById(userId))
                .build();
    }

    @PutMapping("/{userId}")
    public ApiResponse<UserResponse> updateMyProfile(
            @RequestBody @Valid UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .message("User updated successfully.")
                .result(accountService.updateMyProfile(request))
                .build();
    }

    @PutMapping("/me/change-password")
    public ApiResponse<Void> changePassword(
            @Valid @RequestBody ChangePasswordRequest request) {
        accountService.changePassword(request);
        return ApiResponse.<Void>builder()
                .message("Your password changed successfully.")
                .build();
    }

    @PatchMapping("/{userId}/ban")
    public ApiResponse<Void> updateUserStatus(
            @RequestBody @Valid UserUpdateStatusRequest request) {
        accountService.updateUserStatus(request);
        return ApiResponse.<Void>builder()
                .message("User status updated successfully.")
                .build();
    }

}
