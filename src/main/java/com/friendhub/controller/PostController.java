package com.friendhub.controller;

import com.friendhub.dto.request.PostCreationRequest;
import com.friendhub.dto.request.PostUpdateRequest;
import com.friendhub.dto.response.ApiResponse;
import com.friendhub.dto.response.PostResponse;
import com.friendhub.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ApiResponse<PostResponse> createPost(@RequestBody PostCreationRequest request) {
        return ApiResponse.<PostResponse>builder()
                .message("Post created successfully.")
                .result(postService.createPost(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<PostResponse>> getAllPosts() {
        return ApiResponse.<List<PostResponse>>builder()
                .message("Posts retrieved successfully.")
                .result(postService.getAllPosts())
                .build();
    }

    @GetMapping("/{postId}")
    public ApiResponse<PostResponse> getPostById(@PathVariable("postId") long postId) {
        return ApiResponse.<PostResponse>builder()
                .message("Post retrieved successfully.")
                .result(postService.getPostById(postId))
                .build();
    }

    @GetMapping("/author/{authorId}")
    public ApiResponse<List<PostResponse>> getPostByAuthorId(@PathVariable("authorId") long authorId) {
        return ApiResponse.<List<PostResponse>>builder()
                .message("Posts retrieved successfully.")
                .result(postService.getPostsByAuthorId(authorId))
                .build();
    }

    @PutMapping("/{postId}")
    public ApiResponse<PostResponse> updatePost(@PathVariable("postId") long postId,
                                                @RequestBody PostUpdateRequest request) {
        return ApiResponse.<PostResponse>builder()
                .message("Post updated successfully.")
                .result(postService.updatePost(postId, request))
                .build();
    }

    @DeleteMapping("/{postId}")
    public ApiResponse<Void> deletePost(@PathVariable("postId") long postId) {
        postService.deletePost(postId);

        return ApiResponse.<Void>builder()
                .message("Post deleted successfully.")
                .build();
    }

}
