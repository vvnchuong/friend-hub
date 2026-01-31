package com.friendhub.dto.request;

import com.friendhub.enums.ReportReason;
import com.friendhub.enums.TargetType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportCreationRequest {

    TargetType targetType;
    ReportReason reportReason;
    long targetId;

}
