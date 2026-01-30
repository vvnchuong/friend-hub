package com.friendhub.controller;

import com.friendhub.dto.request.*;
import com.friendhub.dto.response.ApiResponse;
import com.friendhub.dto.response.AuthenticationResponse;
import com.friendhub.dto.response.IntrospectResponse;
import com.friendhub.dto.response.UserResponse;
import com.friendhub.service.AuthenticationService;
import com.friendhub.service.AccountService;
import com.friendhub.service.PasswordResetService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final AccountService registerService;
    private final PasswordResetService passwordResetService;

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> login(
            @RequestBody AuthenticationRequest request) {
        return ApiResponse.<AuthenticationResponse>builder()
                .message("Login successful.")
                .result(authenticationService.authenticate(request))
                .build();
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(
            @RequestBody IntrospectRequest request) {
        return ApiResponse.<IntrospectResponse>builder()
                .message("Token is valid.")
                .result(authenticationService.introspect(request))
                .build();
    }

    @PostMapping("/register")
    public ApiResponse<UserResponse> register(
            @RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(registerService.register(request))
                .message("User registered successfully.")
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<AuthenticationResponse> logout(
            @RequestBody LogoutRequest request)
            throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .message("Logout successful.")
                .build();
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthenticationResponse> refreshToken(
            @RequestBody RefreshTokenRequest request)
            throws ParseException, JOSEException {
        return ApiResponse.<AuthenticationResponse>builder()
                .message("Token refreshed successfully.")
                .result(authenticationService.refreshToken(request))
                .build();
    }

    @PostMapping("/forgot-password")
    public ApiResponse<Void> forgotPassword(
            @RequestBody ForgotPasswordRequest request) {
        passwordResetService.forgotPassword(request.getEmail());
        return ApiResponse.<Void>builder()
                .message("Password reset email sent.")
                .build();
    }

    @PostMapping("/reset-password")
    public ApiResponse<Void> resetPassword(
            @RequestBody ResetPasswordRequest request) {
        passwordResetService.resetPassword(
                request.getToken(),
                request.getNewPassword(),
                request.getConfirmPassword());
        return ApiResponse.<Void>builder()
                .message("Password reset successfully.")
                .build();
    }

}
