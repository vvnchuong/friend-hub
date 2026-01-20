package com.friendhub.controller;

import com.friendhub.dto.request.CommentCreationRequest;
import com.friendhub.dto.response.ApiResponse;
import com.friendhub.dto.response.CommentResponse;
import com.friendhub.service.GroupCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/groups")
public class GroupCommentController {

    private final GroupCommentService groupCommentService;

    @PostMapping("/{groupId}/posts/{postId}/comments")
    public ApiResponse<CommentResponse> createComment(
            @PathVariable("groupId") long groupId,
            @PathVariable("postId") long postId,
            @RequestBody CommentCreationRequest request) {
        return ApiResponse.<CommentResponse>builder()
                .message("Comment added successfully.")
                .result(groupCommentService.createCommentInGroup(groupId, postId, request))
                .build();
    }

    @GetMapping("/{groupId}/posts/{postId}/comments")
    public ApiResponse<List<CommentResponse>> getAllComments(
            @PathVariable("groupId") long groupId,
            @PathVariable("postId") long postId) {
        return ApiResponse.<List<CommentResponse>>builder()
                .message("Comment added successfully.")
                .result(groupCommentService.getAllCommentsByGroupIdAndPostId(groupId, postId))
                .build();
    }

}
