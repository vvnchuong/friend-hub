package com.friendhub.controller;

import com.friendhub.dto.request.ReportCreationRequest;
import com.friendhub.dto.response.ApiResponse;
import com.friendhub.service.UserRepostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReportController {

    private final UserRepostService userRepostService;

    @PostMapping
    public ApiResponse<Void> createReport(
            @RequestBody ReportCreationRequest request) {
        userRepostService.createReport(request);
        return ApiResponse.<Void>builder()
                .message("Report created successfully.")
                .build();
    }

}
