package com.friendhub.service.impl;

import com.friendhub.dto.response.PostLikeResponse;
import com.friendhub.entity.Post;
import com.friendhub.entity.PostLike;
import com.friendhub.entity.User;
import com.friendhub.enums.ErrorCode;
import com.friendhub.exception.AppException;
import com.friendhub.repository.PostLikeRepository;
import com.friendhub.repository.PostRepository;
import com.friendhub.repository.UserRepository;
import com.friendhub.service.PostLikeService;
import com.friendhub.utils.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostLikeServiceImpl implements PostLikeService {

    private final PostLikeRepository postLikeRepository;

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    @Override
    @Transactional
    public PostLikeResponse likeOrUnLikePost(long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        User user = userRepository.findById(CurrentUser.id())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Optional<PostLike> existingLike = postLikeRepository
                .findByPostIdAndUserId(postId, user.getId());

        boolean isLiked;

        if (existingLike.isPresent()) {
            postLikeRepository.delete(existingLike.get());
            isLiked = false;
        } else {
            PostLike postLike = PostLike.builder()
                    .post(post)
                    .user(user)
                    .createdAt(Instant.now())
                    .build();

            isLiked = true;

            postLikeRepository.save(postLike);
        }

        int totalLikes = postLikeRepository.countByPostId(postId);

        return PostLikeResponse.builder()
                .postId(postId)
                .userId(user.getId())
                .totalLikes(totalLikes)
                .isLiked(isLiked)
                .build();
    }

}
