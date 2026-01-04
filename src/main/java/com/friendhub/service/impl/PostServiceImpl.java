package com.friendhub.service.impl;

import com.friendhub.entity.Post;
import com.friendhub.entity.PostMedia;
import com.friendhub.entity.User;
import com.friendhub.enums.*;
import com.friendhub.exception.AppException;
import com.friendhub.repository.*;
import com.friendhub.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostMediaRepository postMediaRepository;

    @Override
    public Post createPost(Post post, List<PostMedia> mediaList) {
        postRepository.save(post);

        if (mediaList != null && !mediaList.isEmpty()) {
            mediaList.forEach(media -> media.setPost(post));
            postMediaRepository.saveAll(mediaList);
            post.setPostMedia(mediaList);
        }

        return post;
    }

    @Override
    public Post getPostById(long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
    }

    @Override
    public List<Post> getMyPosts(long userId, Long lastId, int limit) {
        return postRepository
                .findAllMyPosts(userId, lastId, limit);
    }

    // get all posts in profile of a user
    public List<Post> getPostsOfUser(long userId, Long lastId, long limit) {
        return postRepository
                .findAllPostsOfUser(userId, lastId, limit);
    }

    // get all my posts and my friends posts
    @Override
    public List<Post> getAllMyFriendsAndMyPosts(long userId) {
        return postRepository.findAllFriendAndMyPosts(userId);
    }

    @Override
    public void updatePost(Post post) {
        postRepository.save(post);
    }

    @Override
    public void deletePost(long postId) {
        postRepository.deleteById(postId);
    }

    @Override
    public void updateCommentPolicy(Post post) {
        post.setCommentPolicy(post.getCommentPolicy());
    }

    @Override
    public boolean isExistedById(long postId) {
        return postRepository.existsById(postId);
    }

    @Override
    public void validateReportable(long postId, User reporter) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        if (post.getUser().getId() == reporter.getId())
            throw new AppException(ErrorCode.CANNOT_REPORT_OWN_CONTENT);

        if (post.getPrivacy().equals(Privacy.PRIVATE))
            throw new AppException(ErrorCode.UNAUTHORIZED);
    }

    @Override
    public Long countTotalPostsOfUser(long userId) {
        return postRepository.countByUserId(userId);
    }

    @Override
    public Long countTotalPostsByGroupId(long groupId) {
        return postRepository.countByGroupId(groupId);
    }

}
