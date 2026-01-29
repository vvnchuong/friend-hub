package com.friendhub.service.impl;

import com.friendhub.dto.request.CollectionCreationRequest;
import com.friendhub.dto.request.CollectionUpdateRequest;
import com.friendhub.dto.response.CollectionResponse;
import com.friendhub.dto.response.CursorResponse;
import com.friendhub.entity.Collection;
import com.friendhub.entity.CollectionPost;
import com.friendhub.entity.Post;
import com.friendhub.entity.User;
import com.friendhub.enums.ErrorCode;
import com.friendhub.exception.AppException;
import com.friendhub.mapper.CollectionMapper;
import com.friendhub.repository.CollectionPostRepository;
import com.friendhub.repository.CollectionRepository;
import com.friendhub.service.CollectionService;
import com.friendhub.service.FriendService;
import com.friendhub.service.PostService;
import com.friendhub.service.UserService;
import com.friendhub.utils.CurrentUser;
import com.friendhub.utils.CursorPaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CollectionServiceImpl implements CollectionService {

    private final CollectionRepository collectionRepository;
    private final CollectionPostRepository collectionPostRepository;
    private final UserService userService;
    private final PostService postService;
    private final FriendService friendService;
    private final CollectionMapper collectionMapper;

    @Override
    @Transactional
    public CollectionResponse addPostToCollection(long collectionId, long postId) {
        Collection collection = collectionRepository
                .findByIdAndUserId(collectionId, CurrentUser.id())
                .orElseThrow(() -> new AppException(ErrorCode.COLLECTION_NOT_FOUND));

        Post post = assertCollection(postId);

        if (Objects.equals(post, null))
            throw new AppException(ErrorCode.POST_NOT_FOUND);

        if (collectionPostRepository.existsByCollection_IdAndPostId(collectionId, postId))
            throw new AppException(ErrorCode.POST_ALREADY_SAVED);

        if (post.getUser().getId() == CurrentUser.id())
            throw new AppException(ErrorCode.CANNOT_SAVE_OWN_POST);

        CollectionPost collectionPost = new CollectionPost();
        collectionPost.setCollection(collection);
        collectionPost.setPostId(postId);

        collectionPostRepository.save(collectionPost);

        return collectionMapper.toCollectionResponse(collection);
    }

    @Override
    @Transactional
    public void removePostFromCollection(long collectionId, long postId) {
        if (!collectionPostRepository
                .existsByCollection_IdAndPostId(collectionId, postId))
            throw new AppException(ErrorCode.COLLECTION_NOT_FOUND);

        collectionPostRepository
                .deleteByCollection_IdAndPostId(collectionId, postId);
    }

    @Override
    @Transactional
    public CollectionResponse createCollection(CollectionCreationRequest request) {
        User user = userService.getUserById(CurrentUser.id());

        if (collectionRepository.existsByName(request.getName()))
            throw new AppException(ErrorCode.COLLECTION_ALREADY_EXISTS);

        Collection collection = collectionMapper.toCollection(request);

        collection.setUser(user);
        collectionRepository.save(collection);
        return collectionMapper.toCollectionResponse(collection);
    }

    @Override
    public CursorResponse<CollectionResponse> getAllCollections(Long lastId) {
        int pageSize = 6;

        return CursorPaginationUtil.execute(
                pageSize,
                () -> collectionRepository
                        .findAllCollectionByUserId(CurrentUser.id(), lastId, pageSize + 1),
                Collection::getId,
                c -> c.stream()
                        .map(collectionMapper::toCollectionResponse)
                        .toList()
        );
    }

    @Override
    public CollectionResponse getCollectionById(long collectionId) {
        Collection collection = collectionRepository
                .findByIdAndUserId(collectionId, CurrentUser.id())
                .orElseThrow(() -> new AppException(ErrorCode.COLLECTION_NOT_FOUND));

        return collectionMapper.toCollectionResponse(collection);
    }

    @Override
    @Transactional
    public CollectionResponse updateCollection(
            long collectionId, CollectionUpdateRequest request) {
        Collection collection = collectionRepository
                .findByIdAndUserId(collectionId, CurrentUser.id())
                .orElseThrow(() -> new AppException(ErrorCode.COLLECTION_NOT_FOUND));

        collectionMapper.updateCollection(collection, request);

        return collectionMapper.toCollectionResponse(collection);
    }

    @Override
    @Transactional
    public void deleteCollection(long collectionId) {
        if (!collectionRepository.existsById(collectionId))
            throw new AppException(ErrorCode.COLLECTION_NOT_FOUND);

        collectionRepository.deleteById(collectionId);
    }

    private Post assertCollection(long postId) {
        Post post = postService.getPostById(postId);
        switch (post.getPrivacy()) {
            case PUBLIC -> {
                return post;
            }
            case FRIEND -> {
                if (!friendService.areFriends(post.getUser().getId(), CurrentUser.id()))
                    throw new AppException(ErrorCode.POST_ACCESS_DENIED);
                return post;
            }
            case PRIVATE -> throw new AppException(ErrorCode.POST_ACCESS_DENIED);
        }
        return null;
    }

}
