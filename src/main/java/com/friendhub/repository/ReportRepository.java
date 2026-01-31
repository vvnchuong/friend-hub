package com.friendhub.repository;

import com.friendhub.entity.Report;
import com.friendhub.enums.TargetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ReportRepository extends JpaRepository<Report, Long>,
        JpaSpecificationExecutor<Report> {

    boolean existsByReporterIdAndTargetTypeAndTargetId(long reportId, TargetType targetType, long targetId);

}
