package com.friendhub.controller;

import com.friendhub.dto.request.AuthenticationRequest;
import com.friendhub.dto.request.IntrospectRequest;
import com.friendhub.dto.request.UserCreationRequest;
import com.friendhub.dto.response.ApiResponse;
import com.friendhub.dto.response.AuthenticationResponse;
import com.friendhub.dto.response.IntrospectResponse;
import com.friendhub.dto.response.UserResponse;
import com.friendhub.service.AuthenticationService;
import com.friendhub.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final AccountService registerService;

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        return ApiResponse.<AuthenticationResponse>builder()
                .message("Login successfully.")
                .result(authenticationService.authenticate(request))
                .build();
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) {
        return ApiResponse.<IntrospectResponse>builder()
                .result(authenticationService.introspect(request))
                .build();
    }

    @PostMapping("/register")
    public ApiResponse<UserResponse> register(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(registerService.register(request))
                .message("User registered successfully.")
                .build();
    }

}
