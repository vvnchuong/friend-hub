package com.friendhub.dto.response;

import com.friendhub.enums.GroupPrivacy;
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
    long createdBy;
    Instant createdAt;
    Instant updatedAt;
    Integer totalMembers;
    Integer totalPosts;
    JoinRequestStatus joinStatus;
    Boolean hasPendingRequest;

}
