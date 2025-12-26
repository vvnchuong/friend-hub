package com.friendhub.dto.request;

import jakarta.validation.constraints.NotBlank;
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
public class ChangePasswordRequest {

    @NotBlank
    String oldPassword;

    @Size(min = 6, message = "New password must be at least 6 characters")
    String newPassword;

    @NotBlank
    String confirmPassword;

}
