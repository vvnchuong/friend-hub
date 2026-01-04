package com.friendhub.dto.response;

import com.friendhub.enums.GroupPrivacy;
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
public class GroupResponse {

    long id;
    String name;
    GroupPrivacy privacy;
    String description;
    String coverUrl;
    UserResponse creator;
    Instant createdAt;
    Instant updatedAt;
    long totalMembers;


}
