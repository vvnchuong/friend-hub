package com.friendhub.service.impl;

import com.friendhub.dto.request.PostCreationRequest;
import com.friendhub.dto.request.PostUpdateRequest;
import com.friendhub.dto.response.PostMediaResponse;
import com.friendhub.dto.response.PostResponse;
import com.friendhub.entity.Post;
import com.friendhub.entity.PostMedia;
import com.friendhub.entity.User;
import com.friendhub.enums.ErrorCode;
import com.friendhub.enums.FriendStatus;
import com.friendhub.enums.UserRole;
import com.friendhub.exception.AppException;
import com.friendhub.mapper.PostMapper;
import com.friendhub.mapper.PostMediaMapper;
import com.friendhub.repository.*;
import com.friendhub.service.PostService;
import com.friendhub.utils.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostMediaRepository postMediaRepository;
    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final FriendRepository friendRepository;
    private final PostMapper postMapper;
    private final PostMediaMapper postMediaMapper;

    @Transactional
    public PostResponse createPost(PostCreationRequest request) {
        Post post = postMapper.toPost(request);

        User user = userRepository.findById(CurrentUser.id())
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        post.setUser(user);

        postRepository.save(post);

        List<PostMedia> postMediaList = request.getMediaList()
                .stream()
                .map(postMediaMapper::toPostMedia)
                .peek(postMedia -> postMedia.setPost(post))
                .collect(Collectors.toList());

        postMediaRepository.saveAll(postMediaList);

        post.setPostMedia(postMediaList);

        return postMapper.toPostResponse(post);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<PostResponse> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::buildPostResponse)
                .toList();
    }

    @Override
    public PostResponse getPostById(long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        switch (post.getPrivacy()) {
            case PUBLIC -> {}
            case FRIEND -> {
                boolean checkFriend = areFriends(post.getUser().getId(), CurrentUser.id());
                if (!checkFriend)
                    throw new AppException(ErrorCode.UNAUTHORIZED);
            }
            case PRIVATE -> {
                boolean isAuthor = Objects.equals(post.getUser().getId(), CurrentUser.id());
                boolean isAdmin = Objects.equals(CurrentUser.role(), UserRole.ADMIN.toString());

                if (!isAuthor && !isAdmin)
                    throw new AppException(ErrorCode.UNAUTHORIZED);
            }
        }

        return buildPostResponse(post);
    }

    @Override
    public List<PostResponse> getMyPosts() {
        return postRepository.findByUserIdOrderByCreatedAtDesc(CurrentUser.id())
                .stream()
                .map(this::buildPostResponse)
                .toList();
    }

    @Override
    public List<PostResponse> getPostsOfUser(long userId) {
        boolean isSelf = CurrentUser.id() == userId;
        boolean isFriend = areFriends(CurrentUser.id(), userId);

        return postRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .filter(post -> {
                    if (isSelf)
                        return true;
                    return switch (post.getPrivacy()) {
                        case PUBLIC -> true;
                        case FRIEND -> isFriend;
                        case PRIVATE -> false;
                    };
                })
                .map(this::buildPostResponse)
                .toList();
    }

    @Override
    public List<PostResponse> getAllFriendPosts() {
        return postRepository.findAllFriendPosts().stream()
                .map(this::buildPostResponse)
                .toList();
    }

    @Override
    public PostResponse updatePost(long postId, PostUpdateRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        if (post.getUser().getId() != CurrentUser.id())
            throw new AppException(ErrorCode.UNAUTHORIZED);

        postMapper.updatePost(post, request);

        postRepository.save(post);

        return postMapper.toPostResponse(post);
    }

    @Override
    public void deletePost(long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        boolean isAuthor = Objects.equals(post.getUser().getId(), CurrentUser.id());
        boolean isAdmin = Objects.equals(CurrentUser.role(), UserRole.ADMIN.toString());

        if (!isAuthor && !isAdmin)
            throw new AppException(ErrorCode.UNAUTHORIZED);

        postRepository.deleteById(postId);
    }

    private boolean areFriends(long u1, long u2) {
        return friendRepository.existsByRequesterIdAndAddresseeIdAndStatus(u1, u2, FriendStatus.ACCEPTED)
                || friendRepository.existsByRequesterIdAndAddresseeIdAndStatus(u2, u1, FriendStatus.ACCEPTED);
    }

    private PostResponse buildPostResponse(Post post) {
        PostResponse res = postMapper.toPostResponse(post);

        List<PostMediaResponse> postMediaList = postMediaRepository
                .findByPostId(post.getId())
                .stream()
                .map(postMediaMapper::toPostMediaResponse)
                .toList();
        res.setMediaList(postMediaList);

        int totalLikes = postLikeRepository.countByPostId(post.getId());
        res.setTotalLikes(totalLikes);

        int totalComments = commentRepository.countByPostId(post.getId());
        res.setTotalComments(totalComments);

        boolean isLiked = postLikeRepository.existsByPostIdAndUserId(post.getId(), CurrentUser.id());
        res.setLiked(isLiked);

        return res;
    }

}
