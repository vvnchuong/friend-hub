package com.friendhub.dto.response;

import com.friendhub.enums.Gender;
import com.friendhub.enums.UserRole;
import com.friendhub.enums.UserStatus;
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
public class AdminUserResponse {

    long id;
    String firstName;
    String lastName;
    Gender gender;
    String email;
    String avatarUrl;
    String coverUrl;
    UserRole role;
    String phoneNumber;
    String address;
    String bio;
    Instant createdAt;
    long totalPosts;
    long totalFriends;
    UserStatus status;

}
