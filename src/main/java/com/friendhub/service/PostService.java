package com.friendhub.service;

import com.friendhub.dto.request.PostCreationRequest;
import com.friendhub.dto.request.PostUpdateRequest;
import com.friendhub.dto.response.PostResponse;

import java.util.List;

public interface PostService {

    PostResponse createPost(PostCreationRequest request);

    List<PostResponse> getAllPosts();

    PostResponse getPostById(long postId);

    List<PostResponse> getMyPosts();

    List<PostResponse> getPostsOfUser(long userId);

    List<PostResponse> getAllFriendPosts();

    PostResponse updatePost(long postId, PostUpdateRequest request);

    void deletePost(long postId);

}
