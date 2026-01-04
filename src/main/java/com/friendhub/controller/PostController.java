package com.friendhub.controller;

import com.friendhub.dto.request.PostCreationRequest;
import com.friendhub.dto.request.PostUpdateRequest;
import com.friendhub.dto.request.UpdateCommentPolicyRequest;
import com.friendhub.dto.response.ApiResponse;
import com.friendhub.dto.response.CursorResponse;
import com.friendhub.dto.response.PostResponse;
import com.friendhub.service.UserPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final UserPostService userPostService;

    @PostMapping
    public ApiResponse<PostResponse> createPost(
            @RequestBody @Valid PostCreationRequest request) {
        return ApiResponse.<PostResponse>builder()
                .message("Post created successfully.")
                .result(userPostService.createPost(request))
                .build();
    }

    @GetMapping("/{postId}")
    public ApiResponse<PostResponse> getPostById(
            @PathVariable("postId") long postId) {
        return ApiResponse.<PostResponse>builder()
                .message("Post retrieved successfully.")
                .result(userPostService.getPostDetail(postId))
                .build();
    }

    @GetMapping("/my")
    public ApiResponse<CursorResponse<PostResponse>> getMyPosts(
            @RequestParam(required = false) Long lastId) {
        return ApiResponse.<CursorResponse<PostResponse>>builder()
                .message("Post retrieved successfully.")
                .result(userPostService.getMyPosts(lastId))
                .build();
    }

    @GetMapping("/users/{userId}")
    public ApiResponse<CursorResponse<PostResponse>> getPostsOfUser(
            @PathVariable("userId") long userId,
            @RequestParam(required = false) Long lastId) {
        return ApiResponse.<CursorResponse<PostResponse>>builder()
                .message("Post retrieved successfully.")
                .result(userPostService.getPostsOfUser(userId, lastId))
                .build();
    }

    @GetMapping("/friends")
    public ApiResponse<List<PostResponse>> getAllFriendAndMyPosts() {
        return ApiResponse.<List<PostResponse>>builder()
                .message("Post retrieved successfully.")
                .result(userPostService.getAllMyFriendsAndMyPosts())
                .build();
    }

    @PutMapping("/{postId}")
    public ApiResponse<PostResponse> updatePost(
            @PathVariable("postId") long postId,
            @RequestBody @Valid PostUpdateRequest request) {
        return ApiResponse.<PostResponse>builder()
                .message("Post updated successfully.")
                .result(userPostService.updatePost(postId, request))
                .build();
    }

    @PatchMapping("/{postId}/comment-policy")
    public ApiResponse<Void> updateMyPostCommentPolicy(
            @PathVariable("postId") long postId,
            @RequestBody @Valid UpdateCommentPolicyRequest request) {
        userPostService.updateCommentPolicy(postId, request);
        return ApiResponse.<Void>builder()
                .message("")
                .build();
    }

    @DeleteMapping("/{postId}")
    public ApiResponse<Void> deletePost(
            @PathVariable("postId") long postId) {
        userPostService.deletePost(postId);
        return ApiResponse.<Void>builder()
                .message("Post deleted successfully.")
                .build();
    }

}
