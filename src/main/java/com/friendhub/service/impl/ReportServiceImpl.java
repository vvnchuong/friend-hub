package com.friendhub.service.impl;

import com.friendhub.dto.request.ReportSearchRequest;
import com.friendhub.entity.Report;
import com.friendhub.entity.User;
import com.friendhub.enums.ErrorCode;
import com.friendhub.enums.ReportStatus;
import com.friendhub.enums.TargetType;
import com.friendhub.exception.AppException;
import com.friendhub.repository.ReportRepository;
import com.friendhub.repository.specification.ReportSpecification;
import com.friendhub.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;

    @Override
    public void createReport(Report report) {
        reportRepository.save(report);
    }

    @Override
    public Report getReportById(long reportId) {
        return reportRepository.findById(reportId)
                .orElseThrow(() -> new AppException(ErrorCode.REPORT_NOT_FOUND));
    }

    @Override
    public Page<Report> getAllReports(
            ReportSearchRequest request,
            Pageable pageable) {
        Specification<Report> spec = ReportSpecification.build(request);

        return reportRepository.findAll(spec, pageable);
    }

    @Override
    public void markResolvedOrRejected(
            long reportId, User admin, ReportStatus status) {
        Report report = getReportById(reportId);
        if (report.getStatus() != ReportStatus.PENDING)
            throw new AppException(ErrorCode.INVALID_REPORT_STATE);

        report.setStatus(status);
        report.setHandledBy(admin);
        report.setResolvedAt(Instant.now());
    }

    @Override
    public boolean isReported(long reportId, TargetType targetType, long targetId) {
        return reportRepository.existsByReporterIdAndTargetTypeAndTargetId(
                reportId, targetType, targetId);

    }

}
