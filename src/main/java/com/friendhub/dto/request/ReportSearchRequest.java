package com.friendhub.dto.request;

import com.friendhub.enums.ReportStatus;
import com.friendhub.enums.TargetType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportSearchRequest {

    String keyword;
    TargetType targetType;
    ReportStatus status;

}
