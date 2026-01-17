package com.friendhub.controller;

import com.friendhub.dto.request.PostUpdateRequest;
import com.friendhub.dto.response.ApiResponse;
import com.friendhub.dto.response.CursorResponse;
import com.friendhub.dto.response.PostResponse;
import com.friendhub.service.AdminGroupPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/groups")
public class AdminGroupPostController {

    private final AdminGroupPostService adminGroupPostService;

    @GetMapping("/{groupId}/posts")
    public ApiResponse<CursorResponse<PostResponse>> getAllPostsInGroup(
            @PathVariable("groupId") long groupId,
            @RequestParam(required = false) Long lastId) {
        return ApiResponse.<CursorResponse<PostResponse>>builder()
                .message("Posts retrieved successfully.")
                .result(adminGroupPostService.getAllPosts(groupId, lastId))
                .build();
    }

    @GetMapping("/{groupId}/posts/{postId}")
    public ApiResponse<PostResponse> getPostById(
            @PathVariable("groupId") long groupId,
            @PathVariable("postId") long postId) {
        return ApiResponse.<PostResponse>builder()
                .message("Post detail retrieved successfully.")
                .result(adminGroupPostService.getPostById(groupId, postId))
                .build();
    }

    @PutMapping("/{groupId}/posts/{postId}")
    public ApiResponse<PostResponse> updatePost(
            @PathVariable("groupId") long groupId,
            @PathVariable("postId") long postId,
            @RequestBody PostUpdateRequest request) {
        return ApiResponse.<PostResponse>builder()
                .message("Post updated successfully.")
                .result(adminGroupPostService.updatePost(groupId, postId, request))
                .build();
    }

    @DeleteMapping("/{groupId}/posts/{postId}")
    public ApiResponse<Void> deletePost(
            @PathVariable("groupId") long groupId,
            @PathVariable("postId") long postId) {
        adminGroupPostService.deletePost(groupId, postId);
        return ApiResponse.<Void>builder()
                .message("Post deleted successfully.")
                .build();
    }

}
