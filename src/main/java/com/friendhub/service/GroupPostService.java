package com.friendhub.service;

import com.friendhub.entity.Post;
import com.friendhub.entity.PostMedia;

import java.util.List;

public interface GroupPostService {

    Post createPost(Post post, List<PostMedia> mediaList);

    List<Post> getAllPosts(long groupId, Long lastId, int limit);

    Post getPostById(long groupId, long postId);

    void updatePost(Post post);

    void deletePost(long postId);

}
