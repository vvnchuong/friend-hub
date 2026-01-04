package com.friendhub.service;

import com.friendhub.entity.Post;
import com.friendhub.entity.PostMedia;
import com.friendhub.entity.User;

import java.util.List;

public interface PostService {

    Post createPost(Post post, List<PostMedia> mediaList);

//    List<PostResponse> getAllPosts();

    Post getPostById(long postId);

    List<Post> getMyPosts(long userId, Long lastId, int pageSize);

    List<Post> getPostsOfUser(long userId, Long lastId, long limit);

    List<Post> getAllMyFriendsAndMyPosts(long userId);

    void updatePost(Post post);

    void deletePost(long postId);

    void updateCommentPolicy(Post post);

    boolean isExistedById(long postId);

    void validateReportable(long postId, User reporter);

    Long getTotalPostsOfUser(long userId);

}
