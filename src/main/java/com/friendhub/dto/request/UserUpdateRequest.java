package com.friendhub.dto.request;

import com.friendhub.enums.UserRole;
import com.friendhub.enums.Gender;
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
public class UserUpdateRequest {

    @NotBlank(message = "First name is required")
    String firstName;

    @NotBlank(message = "Last name is required")
    String lastName;

    Gender gender;

    @NotBlank(message = "Password is required")
    String password;

    String phoneNumber;
    String address;
    String bio;
    String avatarUrl;
    String coverUrl;

}
