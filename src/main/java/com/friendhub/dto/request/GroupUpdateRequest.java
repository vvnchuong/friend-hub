package com.friendhub.dto.request;

import com.friendhub.enums.GroupPrivacy;
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
public class GroupUpdateRequest {

    @Size(min = 1, max = 50, message = "Group name must be 1–50 characters")
    String name;

    GroupPrivacy privacy;

    @Size(max = 100, message = "Description must be at most 100 characters")
    String description;

    String coverUrl;

}
