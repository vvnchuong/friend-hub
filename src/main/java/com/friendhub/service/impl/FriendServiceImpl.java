package com.friendhub.service.impl;

import com.friendhub.dto.request.FriendAcceptRequest;
import com.friendhub.dto.request.FriendCreationRequest;
import com.friendhub.dto.request.FriendRejectRequest;
import com.friendhub.dto.request.UnFriendRequest;
import com.friendhub.dto.response.CursorResponse;
import com.friendhub.dto.response.FriendResponse;
import com.friendhub.entity.Friend;
import com.friendhub.entity.User;
import com.friendhub.enums.ErrorCode;
import com.friendhub.enums.FriendStatus;
import com.friendhub.exception.AppException;
import com.friendhub.mapper.UserMapper;
import com.friendhub.repository.FriendRepository;
import com.friendhub.repository.UserRepository;
import com.friendhub.service.FriendService;
import com.friendhub.utils.CurrentUser;
import com.friendhub.utils.CursorPaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public void addFriendRequest(FriendCreationRequest request) {
        User me = getUser(CurrentUser.id());
        User other = getUser(request.getUserId());

        if (me.getId() == other.getId())
            throw new AppException(ErrorCode.INVALID_JOIN_REQUEST);

        Friend friend = findOrCreate(me, other);

        if (friend.getStatus() == FriendStatus.ACCEPTED)
            throw new AppException(ErrorCode.ALREADY_FRIENDS);

        if (friend.getStatus() == FriendStatus.PENDING)
            throw new AppException(ErrorCode.FRIEND_REQUEST_ALREADY_PENDING);

        if (friend.getStatus() == FriendStatus.BLOCKED)
            throw new AppException(ErrorCode.UNAUTHORIZED);

        friend.setRequester(me);
        friend.setStatus(FriendStatus.PENDING);
        friend.setUpdatedAt(Instant.now());

        friendRepository.save(friend);
    }

    @Override
    public void acceptFriendRequest(FriendAcceptRequest request) {
        User me = getUser(CurrentUser.id());
        User requester = getUser(request.getUserId());

        Friend friend = getFriend(me, requester);

        if (friend.getStatus() != FriendStatus.PENDING ||
                friend.getRequester().getId() != requester.getId())
            throw new AppException(ErrorCode.FRIEND_REQUEST_NOT_FOUND);

        friend.setStatus(FriendStatus.ACCEPTED);
        friend.setUpdatedAt(Instant.now());

        friendRepository.save(friend);
    }

    @Override
    public void rejectFriendRequest(FriendRejectRequest request) {
        User me = getUser(CurrentUser.id());
        User requester = getUser(request.getUserId());

        Friend friend = getFriend(me, requester);

        if (friend.getStatus() != FriendStatus.PENDING ||
                friend.getRequester().getId() != requester.getId())
            throw new AppException(ErrorCode.FRIEND_REQUEST_NOT_FOUND);

        friend.setStatus(FriendStatus.REJECTED);
        friend.setUpdatedAt(Instant.now());

        friendRepository.save(friend);
    }

    @Override
    public void unFriend(UnFriendRequest request) {
        User me = getUser(CurrentUser.id());
        User other = getUser(request.getUserId());

        Friend friend = getFriend(me, other);

        if (friend.getStatus() != FriendStatus.ACCEPTED)
            throw new AppException(ErrorCode.FRIEND_REQUEST_NOT_FOUND);

        friendRepository.delete(friend);
    }

    @Override
    @Transactional(readOnly = true)
    public CursorResponse<FriendResponse> getAllFriends(Long lastId) {
        int pageSize = 12;

        return CursorPaginationUtil.execute(
                pageSize,

                () -> friendRepository.findFriends(
                        CurrentUser.id(),
                        FriendStatus.ACCEPTED.name(),
                        lastId,
                        pageSize + 1
                ),

                Friend::getId,

                friends -> friends.stream()
                        .map(Friend::getRequester)
                        .map(userMapper::toUserResponse)
                        .map(FriendResponse::new)
                        .toList()
        );
    }

    @Override
    public CursorResponse<FriendResponse> getAllFriendRequests(Long lastId) {
        int pageSize = 9;

        return CursorPaginationUtil.execute(
                pageSize,
                () -> friendRepository
                        .findPendingRequests(CurrentUser.id(), lastId, pageSize + 1),
                Friend::getId,
                f -> f.stream()
                        .map(Friend::getRequester)
                        .map(userMapper::toUserResponse)
                        .map(FriendResponse::new)
                        .toList()
        );
    }

    @Override
    public CursorResponse<FriendResponse> getAllPotentialFriends(Long lastId) {
        int pageSize = 9;

        return CursorPaginationUtil.execute(
                pageSize,
                () -> userRepository
                        .findAllPotentialFriends(CurrentUser.id(), lastId, pageSize + 1),
                User::getId,
                u -> u.stream()
                        .map(userMapper::toUserResponse)
                        .map(FriendResponse::new)
                        .toList()
        );
    }

    @Override
    public boolean areFriends(long userId1, long userId2) {
        long lowId = Math.min(userId1, userId2);
        long highId = Math.max(userId1, userId2);

        return friendRepository.existsByUserLowIdAndUserHighIdAndStatus(
                lowId,
                highId,
                FriendStatus.ACCEPTED
        );
    }

    private Friend findOrCreate(User a, User b) {
        long low = Math.min(a.getId(), b.getId());
        long high = Math.max(a.getId(), b.getId());

        return friendRepository
                .findByUserLowIdAndUserHighId(low, high)
                .orElseGet(() -> Friend.builder()
                        .userLow(getUser(low))
                        .userHigh(getUser(high))
                        .createdAt(Instant.now())
                        .build());
    }

    private Friend getFriend(User a, User b) {
        long low = Math.min(a.getId(), b.getId());
        long high = Math.max(a.getId(), b.getId());

        return friendRepository
                .findByUserLowIdAndUserHighId(low, high)
                .orElseThrow(() -> new AppException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));
    }

    private User getUser(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }
}
