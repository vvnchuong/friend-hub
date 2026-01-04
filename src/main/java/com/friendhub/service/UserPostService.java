package com.friendhub.service;

import com.friendhub.dto.request.PostCreationRequest;
import com.friendhub.dto.request.PostUpdateRequest;
import com.friendhub.dto.request.UpdateCommentPolicyRequest;
import com.friendhub.dto.response.CursorResponse;
import com.friendhub.dto.response.PostResponse;
import com.friendhub.entity.Post;
import com.friendhub.entity.PostMedia;
import com.friendhub.entity.User;
import com.friendhub.enums.CommentPolicy;
import com.friendhub.enums.ErrorCode;
import com.friendhub.exception.AppException;
import com.friendhub.mapper.PostMapper;
import com.friendhub.mapper.PostMediaMapper;
import com.friendhub.utils.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserPostService {

    private final PostService postService;
    private final PostQueryService postQueryService;
    private final UserService userService;
    private final FriendService friendService;
    private final PostMapper postMapper;
    private final PostMediaMapper postMediaMapper;

    @Transactional
    public PostResponse createPost(PostCreationRequest request) {
        User user = userService.getUserById(CurrentUser.id());

        Post post = postMapper.toPost(request);
        post.setUser(user);

        List<PostMedia> mediaList = request.getMediaList()
                .stream()
                .map(postMediaMapper::toPostMedia)
                .toList();

        Post saved = postService.createPost(post, mediaList);

        return postMapper.toPostResponse(saved);
    }

    @Transactional(readOnly = true)
    public PostResponse getPostDetail(long postId) {
        Post post = postService.getPostById(postId);

        switch (post.getPrivacy()) {
            case PUBLIC -> {}
            case FRIEND -> {
                boolean checkFriend = friendService
                        .areFriends(post.getUser().getId(), CurrentUser.id());
                if (!checkFriend)
                    throw new AppException(ErrorCode.UNAUTHORIZED);
            }
            case PRIVATE -> {
                boolean isAuthor = Objects.equals(post.getUser().getId(), CurrentUser.id());
                if (!isAuthor)
                    throw new AppException(ErrorCode.UNAUTHORIZED);
            }
        }

        return postQueryService.build(post, CurrentUser.id());
    }

    @Transactional(readOnly = true)
    public CursorResponse<PostResponse> getMyPosts(Long lastId) {
        int pageSize = 2;

        List<Post> posts = postService
                .getMyPosts(CurrentUser.id(), lastId, pageSize + 1);

        boolean hasNext = posts.size() > pageSize;
        if (hasNext)
            posts = posts.subList(0, pageSize);

        Long nextCursor = hasNext ? posts.get(posts.size() - 1).getId() : null;

        List<PostResponse> responses = posts.stream()
                .map(post -> {
                    return postQueryService
                            .build(post, CurrentUser.id());
                })
                .toList();

        return CursorResponse.<PostResponse>builder()
                .data(responses)
                .nextCursor(nextCursor)
                .hasNext(hasNext)
                .build();
    }

    @Transactional(readOnly = true)
    public CursorResponse<PostResponse> getPostsOfUser(
            long userId, Long lastId) {
        boolean isSelf = CurrentUser.id() == userId;
        boolean areFriends = friendService.areFriends(CurrentUser.id(), userId);
        int pageSize = 2;

        List<Post> posts = postService
                .getPostsOfUser(userId, lastId, pageSize + 1);

        boolean hasNext = posts.size() > pageSize;
        if (hasNext)
            posts = posts.subList(0, pageSize);

        Long nextCursor = hasNext ? posts.get(posts.size() - 1).getId() : null;

        List<PostResponse> responses = posts.stream()
                .filter(post -> {
                    if (isSelf)
                        return true;
                    return switch (post.getPrivacy()) {
                        case PUBLIC -> true;
                        case FRIEND -> areFriends;
                        case PRIVATE -> false;
                    };
                })
                .map(post -> {
                    return postQueryService.build(post, CurrentUser.id());
                })
                .toList();

        return CursorResponse.<PostResponse>builder()
                .data(responses)
                .nextCursor(nextCursor)
                .hasNext(hasNext)
                .build();
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllMyFriendsAndMyPosts() {
        return postService.getAllMyFriendsAndMyPosts(CurrentUser.id())
                .stream()
                .map(post -> {
                    return postQueryService.build(post, CurrentUser.id());
                }).toList();
    }

    @Transactional
    public PostResponse updatePost(long postId, PostUpdateRequest request) {
        Post post = postService.getPostById(postId);

        if (post.getUser().getId() != CurrentUser.id())
            throw new AppException(ErrorCode.UNAUTHORIZED);

        postMapper.updatePost(post, request);

        postService.updatePost(post);

        return postMapper.toPostResponse(post);
    }

    @Transactional
    public void deletePost(long postId) {
        Post post = postService.getPostById(postId);

        boolean isAuthor = Objects.equals(post.getUser().getId(), CurrentUser.id());

        if (!isAuthor)
            throw new AppException(ErrorCode.UNAUTHORIZED);

        postService.deletePost(postId);
    }

    @Transactional
    public void updateCommentPolicy(long postId, UpdateCommentPolicyRequest request) {
        Post post = postService.getPostById(postId);

        boolean isAuthor = Objects.equals(post.getUser().getId(), CurrentUser.id());

        if (!isAuthor)
            throw new AppException(ErrorCode.UNAUTHORIZED);

        if (post.getCommentPolicy() == CommentPolicy.DISABLED)
            throw new AppException(ErrorCode.COMMENT_DISABLED_BY_ADMIN);

        post.setCommentPolicy(request.getPolicy());

        postService.updateCommentPolicy(post);
    }

}
