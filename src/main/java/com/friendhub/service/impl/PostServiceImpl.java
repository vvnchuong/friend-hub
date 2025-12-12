package com.friendhub.service.impl;

import com.friendhub.dto.request.PostCreationRequest;
import com.friendhub.dto.request.PostUpdateRequest;
import com.friendhub.dto.response.PostMediaResponse;
import com.friendhub.dto.response.PostResponse;
import com.friendhub.entity.Post;
import com.friendhub.entity.PostMedia;
import com.friendhub.entity.User;
import com.friendhub.enums.ErrorCode;
import com.friendhub.exception.AppException;
import com.friendhub.mapper.PostMapper;
import com.friendhub.mapper.PostMediaMapper;
import com.friendhub.repository.*;
import com.friendhub.service.PostService;
import com.friendhub.utils.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final PostMediaRepository postMediaRepository;

    private final PostLikeRepository postLikeRepository;

    private final UserRepository userRepository;

    private final CommentRepository commentRepository;

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

    public List<PostResponse> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::buildPostResponse)
                .toList();
    }

    public PostResponse getPostById(long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        return postMapper.toPostResponse(post);
    }

    public List<PostResponse> getPostsByAuthorId(long authorId) {
        return postRepository.findByUserIdOrderByCreatedAtDesc(authorId).stream()
                .map(this::buildPostResponse)
                .toList();
    }

    public PostResponse updatePost(long postId, PostUpdateRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        postMapper.updatePost(post, request);

        postRepository.save(post);

        return postMapper.toPostResponse(post);
    }

    public void deletePost(long postId) {
        boolean isExisted = postRepository.existsById(postId);
        if (!isExisted)
            throw new AppException(ErrorCode.POST_NOT_FOUND);

        postRepository.deleteById(postId);
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
