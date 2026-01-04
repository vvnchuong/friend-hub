package com.friendhub.dto.response;

import com.friendhub.enums.GroupPrivacy;
import com.friendhub.enums.GroupRole;
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
public class GroupDetailResponse {

    long id;
    String name;
    String description;
    String coverUrl;
    GroupPrivacy privacy;
    UserBasicInfoResponse creator;
    Instant createdAt;
    Instant updatedAt;
    Integer totalMembers;
    Integer totalPosts;
    Boolean isJoined;
    JoinRequestStatus status;
    GroupRole role;
    Boolean hasPendingRequest;

}
