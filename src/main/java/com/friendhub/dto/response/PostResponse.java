package com.friendhub.dto.response;

import com.friendhub.enums.CommentPolicy;
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
    UserBasicInfoResponse author;
    PostSharedResponse originalPost;
    Instant createdAt;
    Instant updatedAt;
    Privacy privacy;
    CommentPolicy commentPolicy;
    int totalLikes;
    int totalComments;
    boolean isLiked;
    GroupResponse group;

}
