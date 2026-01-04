package com.friendhub.dto.request;

import com.friendhub.enums.GroupPrivacy;
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
public class GroupCreationRequest {

    @NotBlank(message = "Name is required")
    String name;

    GroupPrivacy privacy;

    @NotBlank(message = "Description is required")
    String description;

    String coverUrl;

}
