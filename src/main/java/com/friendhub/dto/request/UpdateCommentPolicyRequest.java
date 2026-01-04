package com.friendhub.dto.request;

import com.friendhub.enums.CommentPolicy;
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
public class UpdateCommentPolicyRequest {

    @NotBlank(message = "Comment policy is required")
    CommentPolicy policy;

}
