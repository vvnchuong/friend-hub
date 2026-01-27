package com.friendhub.dto.request;

import com.friendhub.enums.CommentPolicy;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCommentPolicyRequest {

    @NotNull(message = "Comment policy is required")
    CommentPolicy policy;

}
