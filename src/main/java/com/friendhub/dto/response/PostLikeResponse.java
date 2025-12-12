package com.friendhub.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostLikeResponse {

    long postId;
    long userId;
    int totalLikes;
    boolean isLiked;

}
