package com.friendhub.controller;

import com.friendhub.dto.request.ReportSearchRequest;
import com.friendhub.dto.response.ApiResponse;
import com.friendhub.dto.response.PageResponse;
import com.friendhub.dto.response.ReportResponse;
import com.friendhub.enums.ReportStatus;
import com.friendhub.enums.TargetType;
import com.friendhub.service.AdminReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/reports")
public class AdminReportController {

    private final AdminReportService adminReportService;

    @GetMapping("/{reportId}")
    public ApiResponse<ReportResponse> getReportById(
            @PathVariable("reportId") long reportId) {
        return ApiResponse.<ReportResponse>builder()
                .message("Reports retrieved successfully.")
                .result(adminReportService.getReportById(reportId))
                .build();
    }

    @GetMapping
    public ApiResponse<PageResponse<ReportResponse>> getAllReports(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) TargetType targetType,
            @RequestParam(required = false) ReportStatus status,
            @PageableDefault(size = 5) Pageable pageable) {
        ReportSearchRequest request = ReportSearchRequest.builder()
                .keyword(keyword)
                .targetType(targetType)
                .status(status)
                .build();

        return ApiResponse.<PageResponse<ReportResponse>>builder()
                .message("Reports retrieved successfully.")
                .result(adminReportService.getAllReports(request, pageable))
                .build();
    }

    @PostMapping("/{reportId}/resolve")
    public ApiResponse<Void> resolveReport(
            @PathVariable("reportId") long reportId) {
        adminReportService.resolve(reportId);
        return ApiResponse.<Void>builder()
                .message("Report resolved successfully.")
                .build();
    }

    @PostMapping("/{reportId}/reject")
    public ApiResponse<Void> rejectReport(
            @PathVariable("reportId") long reportId) {
        adminReportService.reject(reportId);
        return ApiResponse.<Void>builder()
                .message("Report rejected successfully.")
                .build();
    }

}
