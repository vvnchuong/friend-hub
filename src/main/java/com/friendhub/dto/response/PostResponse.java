package com.friendhub.dto.response;

import com.friendhub.enums.Privacy;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostResponse {

    long id;
    String content;
    List<PostMediaResponse> mediaList;
    UserResponse author;
    Instant createdAt;
    Instant updatedAt;
    Privacy privacy;
    int totalLikes;
    int totalComments;
    boolean isLiked;

}
