package com.friendhub.dto.response;

import com.friendhub.enums.GroupRole;
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
public class GroupMemberResponse {

    UserBasicInfoResponse user;
    GroupRole role;
    Instant joinedAt;

}
