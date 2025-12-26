package com.friendhub.dto.request;

import com.friendhub.enums.Gender;
import com.friendhub.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "First name is required")
    String firstName;

    @NotBlank(message = "Last name is required")
    String lastName;

    Gender gender;

    String phoneNumber;
    String address;
    String bio;
    String avatarUrl;
    String coverUrl;
    UserRole role;

}
