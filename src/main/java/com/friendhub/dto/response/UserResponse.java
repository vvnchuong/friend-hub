package com.friendhub.dto.response;

import com.friendhub.enums.UserRole;
import com.friendhub.enums.Gender;
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
public class UserResponse {

    long id;
    String firstName;
    String lastName;
    Gender gender;
    String email;
    String avatarUrl;
    String coverUrl;
    String phoneNumber;
    String address;
    String bio;
    Instant createdAt;

}
