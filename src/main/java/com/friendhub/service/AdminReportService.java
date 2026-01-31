package com.friendhub.service;

import com.friendhub.dto.request.ReportSearchRequest;
import com.friendhub.dto.response.PageResponse;
import com.friendhub.dto.response.ReportResponse;
import com.friendhub.entity.Report;
import com.friendhub.entity.User;
import com.friendhub.enums.ReportStatus;
import com.friendhub.mapper.ReportMapper;
import com.friendhub.utils.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminReportService {

    private final UserService userService;
    private final ReportService reportService;
    private final ReportMapper reportMapper;

    @Transactional(readOnly = true)
    public ReportResponse getReportById(long reportId) {
        Report report = reportService.getReportById(reportId);

        return reportMapper.toResponse(report);
    }

    @Transactional(readOnly = true)
    public PageResponse<ReportResponse> getAllReports(
            ReportSearchRequest request, Pageable pageable) {

        Page<Report> page = reportService.getAllReports(request, pageable);

        return PageResponse.<ReportResponse>builder()
                .data(page.getContent()
                        .stream()
                        .map(reportMapper::toResponse)
                        .toList())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    @Transactional
    public void resolve(long reportId) {
        User admin = userService.getUserById(CurrentUser.id());

        reportService.markResolvedOrRejected(
                reportId, admin, ReportStatus.RESOLVED);
    }

    @Transactional
    public void reject(long reportId) {
        User admin = userService.getUserById(CurrentUser.id());

        reportService.markResolvedOrRejected(
                reportId, admin, ReportStatus.REJECTED);
    }

}
