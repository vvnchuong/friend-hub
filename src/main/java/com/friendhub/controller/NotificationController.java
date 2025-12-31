package com.friendhub.controller;

import com.friendhub.dto.response.ApiResponse;
import com.friendhub.dto.response.NotificationResponse;
import com.friendhub.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ApiResponse<List<NotificationResponse>> getMyNotifications() {
        return ApiResponse.<List<NotificationResponse>>builder()
                .message("Notification retrieved successfully.")
                .result(notificationService.getMyNotifications())
                .build();
    }

    @GetMapping("/unseen-count")
    public ApiResponse<Integer> countUnreadNotifications() {
        return ApiResponse.<Integer>builder()
                .message("Unseen notifications count retrieved successfully.")
                .result(notificationService.countUnreadNotifications())
                .build();
    }

}
