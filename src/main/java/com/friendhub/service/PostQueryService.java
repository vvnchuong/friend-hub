package com.friendhub.service;

import com.friendhub.dto.response.PostResponse;
import com.friendhub.entity.Post;
import com.friendhub.mapper.PostMapper;
import com.friendhub.mapper.PostMediaMapper;
import com.friendhub.repository.CommentRepository;
import com.friendhub.repository.PostLikeRepository;
import com.friendhub.repository.PostMediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostQueryService {

    private final PostMediaRepository postMediaRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;
    private final PostMapper postMapper;
    private final PostMediaMapper postMediaMapper;

    public PostResponse build(Post post, long viewerId) {
        PostResponse res = postMapper.toPostResponse(post);

        res.setMediaList(
                postMediaRepository.findByPostId(post.getId())
                        .stream()
                        .map(postMediaMapper::toPostMediaResponse)
                        .toList()
        );

        res.setTotalLikes(postLikeRepository.countByPostId(post.getId()));
        res.setTotalComments(commentRepository.countByPostId(post.getId()));
        res.setLiked(
                postLikeRepository.existsByPostIdAndUserId(post.getId(), viewerId)
        );

        return res;
    }

}
