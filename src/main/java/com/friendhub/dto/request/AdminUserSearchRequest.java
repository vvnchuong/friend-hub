package com.friendhub.dto.request;

import com.friendhub.enums.Gender;
import com.friendhub.enums.UserRole;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminUserSearchRequest {

    String keyword;
    UserRole role;
    Gender gender;
}