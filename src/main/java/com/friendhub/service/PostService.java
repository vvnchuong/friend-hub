package com.friendhub.service;

import com.friendhub.entity.Post;
import com.friendhub.entity.PostMedia;
import com.friendhub.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface PostService {

    Post createPost(Post post, List<PostMedia> mediaList);

    Page<Post> getAllPosts(Specification<Post> spec, Pageable pageable);

    List<Post> getPostByUserAndGroup(long userId, long groupId);

    Post getPostById(long postId);

    List<Post> getMyPosts(long userId, Long lastId, int pageSize);

    List<Post> getPostsOfUser(long userId, Long lastId, long limit);

    List<Post> getFeed(long userId, Long lastId, int limit);

    void updatePost(Post post);

    void deletePost(long postId);

    void deleteAllPosts(List<Post> posts);

    void updateCommentPolicy(Post post);

    boolean isExistedById(long postId);

    void validateReportable(long postId, User reporter);

    Long countTotalPostsOfUser(long userId);

    Long countTotalPostsByGroupId(long groupId);

}
