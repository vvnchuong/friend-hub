package com.friendhub.controller;

import com.friendhub.dto.request.CommentCreationRequest;
import com.friendhub.dto.response.ApiResponse;
import com.friendhub.dto.response.CommentResponse;
import com.friendhub.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}/comments")
    public ApiResponse<CommentResponse> createComment(@PathVariable("postId") long postId,
                                                      @RequestBody CommentCreationRequest request) {
        return ApiResponse.<CommentResponse>builder()
                .message("Comment added successfully.")
                .result(commentService.createComment(postId, request))
                .build();
    }

    @GetMapping("/{postId}/comments")
    public ApiResponse<List<CommentResponse>> getAllCommentsByPostId(@PathVariable("postId") long postId) {
        return ApiResponse.<List<CommentResponse>>builder()
                .message("Comments retrieved successfully.")
                .result(commentService.getAllCommentsByPostId(postId))
                .build();
    }


}
