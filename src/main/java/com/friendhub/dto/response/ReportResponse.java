package com.friendhub.dto.response;

import com.friendhub.enums.ReportReason;
import com.friendhub.enums.ReportStatus;
import com.friendhub.enums.TargetType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportResponse {

    UserBasicInfoResponse reporter;
    TargetType targetType;
    ReportReason reportReason;
    ReportStatus status;
    long targetId;
    UserBasicInfoResponse handledBy;
    Instant createdAt;
    Instant resolvedAt;

}
