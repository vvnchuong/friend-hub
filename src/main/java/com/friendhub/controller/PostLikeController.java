package com.friendhub.controller;

import com.friendhub.dto.response.ApiResponse;
import com.friendhub.dto.response.PostLikeResponse;
import com.friendhub.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PostMapping("/{postId}/like")
    public ApiResponse<PostLikeResponse> likePost(@PathVariable("postId") long postId) {
        PostLikeResponse response = postLikeService.likeOrUnLikePost(postId);

        String message = response.isLiked()
                ? "Post liked."
                : "Post unliked.";

        return ApiResponse.<PostLikeResponse>builder()
                .result(response)
                .message(message)
                .build();
    }

}
