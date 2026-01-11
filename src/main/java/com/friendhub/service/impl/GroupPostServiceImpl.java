package com.friendhub.service.impl;

import com.friendhub.entity.*;
import com.friendhub.enums.ErrorCode;
import com.friendhub.exception.AppException;
import com.friendhub.repository.*;
import com.friendhub.service.GroupPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupPostServiceImpl implements GroupPostService {

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
    public List<Post> getAllPosts(long groupId, Long lastId, int limit) {
        return postRepository.findAllPostsInGroup(groupId, lastId, limit);
    }

    public Post getPostById(long groupId, long postId) {
        return postRepository.findByIdAndGroupId(postId, groupId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
    }

    @Override
    public void updatePost(Post post) {
        postRepository.save(post);
    }


    @Override
    public void deletePost(long postId) {
        postRepository.deleteById(postId);
    }

}
