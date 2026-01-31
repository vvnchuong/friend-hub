package com.friendhub.controller;

import com.friendhub.dto.response.ApiResponse;
import com.friendhub.dto.response.DashboardSummaryResponse;
import com.friendhub.service.impl.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    public ApiResponse<DashboardSummaryResponse> summaryResponse() {
        return ApiResponse.<DashboardSummaryResponse>builder()
                .message("Summary retrieved successfully.")
                .result(dashboardService.summaryResponse())
                .build();
    }

}
