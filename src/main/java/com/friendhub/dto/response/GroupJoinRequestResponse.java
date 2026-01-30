package com.friendhub.dto.response;

import com.friendhub.enums.JoinRequestStatus;
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
public class GroupJoinRequestResponse {

    long id;
    long groupId;
    String groupName;
    String coverUrl;
    UserBasicInfoResponse user;
    JoinRequestStatus status;
    Instant createdAt;
    UserBasicInfoResponse handledBy;

}
