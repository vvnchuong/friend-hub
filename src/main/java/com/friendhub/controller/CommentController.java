package com.friendhub.controller;

import com.friendhub.dto.request.CommentCreationRequest;
import com.friendhub.dto.request.UpdateCommentPolicyRequest;
import com.friendhub.dto.response.ApiResponse;
import com.friendhub.dto.response.CommentResponse;
import com.friendhub.dto.response.CursorResponse;
import com.friendhub.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}/comments")
    public ApiResponse<CommentResponse> createComment(
            @PathVariable("postId") long postId,
            @RequestBody CommentCreationRequest request) {
        return ApiResponse.<CommentResponse>builder()
                .message("Comment added successfully.")
                .result(commentService.createComment(postId, request))
                .build();
    }

    @GetMapping("/{postId}/comments")
    public ApiResponse<CursorResponse<CommentResponse>> getAllComments(
            @PathVariable("postId") long postId,
            @RequestParam(required = false) Long lastId) {
        return ApiResponse.<CursorResponse<CommentResponse>>builder()
                .message("Comments retrieved successfully.")
                .result(commentService.getAllCommentsPost(postId, lastId))
                .build();
    }

    @PatchMapping("/{postId}/comment-policy")
    public ApiResponse<Void> updateMyPostCommentPolicy(
            @PathVariable("postId") long postId,
            @RequestBody @Valid UpdateCommentPolicyRequest request) {
        commentService.updateCommentPolicy(postId, request);
        return ApiResponse.<Void>builder()
                .message("Comment policy updated successfully.")
                .build();
    }

}
