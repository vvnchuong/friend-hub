package com.friendhub.service;

import com.friendhub.dto.request.ReportCreationRequest;
import com.friendhub.entity.Report;
import com.friendhub.entity.User;
import com.friendhub.enums.ErrorCode;
import com.friendhub.enums.ReportStatus;
import com.friendhub.exception.AppException;
import com.friendhub.mapper.ReportMapper;
import com.friendhub.utils.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserRepostService {

    private final ReportService reportService;
    private final UserService userService;
    private final PostService postService;
    private final ReportMapper reportMapper;

    @Transactional
    public void createReport(ReportCreationRequest request) {
        User reporter = userService.getUserById(CurrentUser.id());

//        switch (request.getTargetType()) {
//            case POST -> postService.validateReportable(request.getTargetId(), reporter);
//            case GROUP -> {
//                return;
//            }
//        }

        postService.validateReportable(request.getTargetId(), reporter);

        if (reportService.isReported(
                        reporter.getId(),
                        request.getTargetType(),
                        request.getTargetId()))
            throw new AppException(ErrorCode.REPORT_ALREADY_EXISTS);

        Report report = reportMapper.toReport(request, reporter);
        report.setStatus(ReportStatus.PENDING);

        reportService.createReport(report);
    }

}
