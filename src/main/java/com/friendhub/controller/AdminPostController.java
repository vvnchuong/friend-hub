package com.friendhub.controller;

import com.friendhub.dto.request.UpdateCommentPolicyRequest;
import com.friendhub.dto.response.ApiResponse;
import com.friendhub.dto.response.PostResponse;
import com.friendhub.service.AdminPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/posts")
public class AdminPostController {

    private final AdminPostService adminPostService;

//    @GetMapping
//    public ApiResponse<List<PostResponse>> getAllPosts() {
//        return ApiResponse.<List<PostResponse>>builder()
//                .message("Posts retrieved successfully.")
//                .result(postService.getAllPosts())
//                .build();
//    }

    @GetMapping("/{postId}")
    public ApiResponse<PostResponse> getPostById(
            @PathVariable("postId") long postId) {
        return ApiResponse.<PostResponse>builder()
                .message("Post retrieved successfully.")
                .result(adminPostService.getPostDetail(postId))
                .build();
    }

    @PostMapping("/{postId}/comment-policy")
    public ApiResponse<Void> adminUpdateCommentPolicy(
            @PathVariable("postId") long postId,
            @RequestBody @Valid UpdateCommentPolicyRequest request) {
        adminPostService.updateCommentPolicy(postId, request);
        return ApiResponse.<Void>builder()
                .message("Post updated comment policy successfully.")
                .build();
    }

    @DeleteMapping("/{postId}")
    public ApiResponse<Void> deletePost(@PathVariable("postId") long postId) {
        adminPostService.deletePost(postId);

        return ApiResponse.<Void>builder()
                .message("Post deleted successfully.")
                .build();
    }

}
