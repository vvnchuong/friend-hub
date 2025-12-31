package com.friendhub.controller;

import com.friendhub.dto.request.AdminUserCreationRequest;
import com.friendhub.dto.request.AdminUserSearchRequest;
import com.friendhub.dto.request.AdminUserUpdateRequest;
import com.friendhub.dto.request.BanUserRequest;
import com.friendhub.dto.response.AdminUserResponse;
import com.friendhub.dto.response.ApiResponse;
import com.friendhub.dto.response.PageResponse;
import com.friendhub.dto.response.UserResponse;
import com.friendhub.enums.Gender;
import com.friendhub.enums.UserRole;
import com.friendhub.service.AdminUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @PostMapping
    public ApiResponse<UserResponse> createUser(
            @RequestBody @Valid AdminUserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .message("User created successfully.")
                .result(adminUserService.createUser(request))
                .build();
    }

    @GetMapping
    public ApiResponse<PageResponse<AdminUserResponse>> getAllUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) UserRole role,
            @RequestParam(required = false) Gender gender,
            @PageableDefault(size = 2) Pageable pageable) {
        AdminUserSearchRequest request = AdminUserSearchRequest.builder()
                .keyword(keyword)
                .role(role)
                .gender(gender)
                .build();

        return ApiResponse.<PageResponse<AdminUserResponse>>builder()
                .message("Users retrieved successfully.")
                .result(adminUserService.getAllUsers(request, pageable))
                .build();
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserResponse> getUserById(
            @PathVariable("userId") long userId) {
        return ApiResponse.<UserResponse>builder()
                .message("User retrieved successfully.")
                .result(adminUserService.getUserDetail(userId))
                .build();
    }

    @PutMapping("/{userId}")
    public ApiResponse<UserResponse> updateUser(
            @PathVariable("userId") long userId,
            @RequestBody @Valid AdminUserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .message("User updated successfully.")
                .result(adminUserService.updateUser(userId, request))
                .build();
    }

    @DeleteMapping("/{userId}")
    public ApiResponse<Void> deleteUser(
            @PathVariable("userId") long userId) {
        adminUserService.deleteUser(userId);
        return ApiResponse.<Void>builder()
                .message("User deleted successfully.")
                .build();
    }

    @PostMapping("/{userId}/ban")
    public ApiResponse<Void> banUser(
            @PathVariable("userId") long userId,
            @RequestBody BanUserRequest request) {
        adminUserService.banUser(userId, request);
        return ApiResponse.<Void>builder()
                .message("User banned successfully.")
                .build();
    }

    @PostMapping("/{userId}/unban")
    public ApiResponse<Void> unBanUser(
            @PathVariable("userId") long userId) {
        adminUserService.unBanUser(userId);
        return ApiResponse.<Void>builder()
                .message("User unbanned successfully.")
                .build();
    }


}
