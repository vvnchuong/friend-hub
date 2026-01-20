package com.friendhub.controller;

import com.friendhub.dto.response.ApiResponse;
import com.friendhub.dto.response.PostLikeResponse;
import com.friendhub.service.GroupPostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/groups")
public class GroupPostLikeController {

    private final GroupPostLikeService groupPostLikeService;

    @PostMapping("/{groupId}/posts/{postId}/likes")
    public ApiResponse<PostLikeResponse> likeOrUnlikePostInGroup(
            @PathVariable("groupId") long groupId,
            @PathVariable("postId") long postId) {
        PostLikeResponse response = groupPostLikeService.likeOrUnlikePostInGroup(groupId, postId);

        String message = response.isLiked()
                ? "Post liked."
                : "Post unliked.";

        return ApiResponse.<PostLikeResponse>builder()
                .result(response)
                .message(message)
                .build();
    }

}
