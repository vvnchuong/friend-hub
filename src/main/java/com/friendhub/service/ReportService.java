package com.friendhub.service;

import com.friendhub.dto.request.ReportSearchRequest;
import com.friendhub.entity.Report;
import com.friendhub.entity.User;
import com.friendhub.enums.ReportStatus;
import com.friendhub.enums.TargetType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportService {

    void createReport(Report report);

    Report getReportById(long reportId);

    Page<Report> getAllReports(ReportSearchRequest request, Pageable pageable);

    void markResolvedOrRejected(long reportId, User admin, ReportStatus status);

    boolean isReported(long reportId, TargetType targetType, long targetId);

}
