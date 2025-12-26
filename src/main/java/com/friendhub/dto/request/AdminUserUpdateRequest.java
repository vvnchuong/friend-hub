package com.friendhub.dto.request;

import com.friendhub.enums.Gender;
import com.friendhub.enums.UserRole;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminUserUpdateRequest {

    @Size(min = 2, max = 50)
    String firstName;

    @Size(min = 2, max = 50)
    String lastName;

    Gender gender;

    String phoneNumber;
    String address;
    String bio;
    String avatarUrl;
    String coverUrl;
    UserRole role;

}
