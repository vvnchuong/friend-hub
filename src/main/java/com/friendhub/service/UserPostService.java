package com.friendhub.service;

import com.friendhub.dto.request.PostCreationRequest;
import com.friendhub.dto.request.PostUpdateRequest;
import com.friendhub.dto.request.SharePostRequest;
import com.friendhub.dto.response.CursorResponse;
import com.friendhub.dto.response.PostResponse;
import com.friendhub.entity.Post;
import com.friendhub.entity.PostMedia;
import com.friendhub.entity.User;
import com.friendhub.enums.ErrorCode;
import com.friendhub.enums.Privacy;
import com.friendhub.exception.AppException;
import com.friendhub.mapper.PostMapper;
import com.friendhub.mapper.PostMediaMapper;
import com.friendhub.utils.CurrentUser;
import com.friendhub.utils.CursorPaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
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
        int pageSize = 10;

        return CursorPaginationUtil.execute(
                pageSize,
                () -> postService
                        .getMyPosts(CurrentUser.id(), lastId, pageSize + 1),
                Post::getId,
                p -> p.stream()
                        .map(post -> postQueryService
                                .build(post, CurrentUser.id()))
                        .toList()
        );
    }

    @Transactional(readOnly = true)
    public CursorResponse<PostResponse> getPostsOfUser(
            long userId, Long lastId) {
        boolean isSelf = CurrentUser.id() == userId;
        boolean areFriends = friendService.areFriends(CurrentUser.id(), userId);
        int pageSize = 10;

        return CursorPaginationUtil.execute(
                pageSize,
                () -> postService
                        .getPostsOfUser(userId, lastId, pageSize + 1),
                Post::getId,
                p -> p.stream()
                        .filter(post -> {
                            if (isSelf)
                                return true;
                            return switch (post.getPrivacy()) {
                                case PUBLIC -> true;
                                case FRIEND -> areFriends;
                                case PRIVATE -> false;
                            };
                        })
                        .map(post -> postQueryService.build(post, CurrentUser.id()))
                        .toList()
        );
    }

    @Transactional(readOnly = true)
    public CursorResponse<PostResponse> getFeed(Long lastId) {
        int pageSize = 10;

        return CursorPaginationUtil.execute(
                pageSize,
                () -> postService
                        .getFeed(CurrentUser.id(), lastId, pageSize + 1),
                Post::getId,
                p -> p.stream()
                        .map(post -> postQueryService.build(post, CurrentUser.id()))
                        .toList()
        );
    }

    @Transactional
    public PostResponse updatePost(long postId, PostUpdateRequest request) {
        Post post = postService.getPostById(postId);

        if (post.getUser().getId() != CurrentUser.id())
            throw new AppException(ErrorCode.UNAUTHORIZED);

        postMapper.updatePost(post, request);

        postService.updatePost(post);

        post.setUpdatedAt(Instant.now());

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
    public PostResponse sharePost(long originalPostId, SharePostRequest request) {
        User user = userService.getUserById(CurrentUser.id());

        Post originalPost = postService.getPostById(originalPostId);

        if (originalPost.getPrivacy() == Privacy.PRIVATE ||
                originalPost.getGroup() != null)
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        if (Objects.equals(originalPost.getUser().getId(), CurrentUser.id()))
            throw new AppException(ErrorCode.CANNOT_SHARE_OWN_POST);

        if (originalPost.getPrivacy() == Privacy.FRIEND &&
                !friendService.areFriends(originalPost.getUser().getId(), CurrentUser.id()))
            throw new AppException(ErrorCode.NOT_FRIENDS);

        Post sharePost = new Post();
        sharePost.setUser(user);
        sharePost.setContent(request.getContent());
        sharePost.setPrivacy(request.getPrivacy());
        sharePost.setOriginalPost(originalPost);

        Post saved = postService.createPost(sharePost, List.of());

        return postQueryService.build(saved, CurrentUser.id());
    }


}
