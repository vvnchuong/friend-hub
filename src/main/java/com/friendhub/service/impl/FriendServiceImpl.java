package com.friendhub.service.impl;

import com.friendhub.dto.request.FriendAcceptRequest;
import com.friendhub.dto.request.FriendCreationRequest;
import com.friendhub.dto.request.FriendRejectRequest;
import com.friendhub.dto.request.UnFriendRequest;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

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

        if (me.getId() == other.getId()) {
            throw new AppException(ErrorCode.INVALID_JOIN_REQUEST);
        }

        Friend friend = findOrCreate(me, other);

        if (friend.getStatus() == FriendStatus.ACCEPTED) {
            throw new AppException(ErrorCode.ALREADY_FRIENDS);
        }
        if (friend.getStatus() == FriendStatus.PENDING) {
            throw new AppException(ErrorCode.FRIEND_REQUEST_ALREADY);
        }
        if (friend.getStatus() == FriendStatus.BLOCKED) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

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
                friend.getRequester().getId() != requester.getId()) {
            throw new AppException(ErrorCode.FRIEND_REQUEST_NOT_FOUND);
        }

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
                friend.getRequester().getId() != requester.getId()) {
            throw new AppException(ErrorCode.FRIEND_REQUEST_NOT_FOUND);
        }

        friend.setStatus(FriendStatus.REJECTED);
        friend.setUpdatedAt(Instant.now());

        friendRepository.save(friend);
    }

    @Override
    public void unFriend(UnFriendRequest request) {
        User me = getUser(CurrentUser.id());
        User other = getUser(request.getUserId());

        Friend friend = getFriend(me, other);

        if (friend.getStatus() != FriendStatus.ACCEPTED) {
            throw new AppException(ErrorCode.FRIEND_REQUEST_NOT_FOUND);
        }

        friendRepository.delete(friend);
    }

    @Override
    public List<FriendResponse> getAllFriendsByUser() {
        return friendRepository
                .findFriends(CurrentUser.id(), FriendStatus.ACCEPTED)
                .stream()
                .map(f ->
                        f.getUserLow().getId() == CurrentUser.id()
                                ? f.getUserHigh()
                                : f.getUserLow()
                )
                .map(userMapper::toUserResponse)
                .map(FriendResponse::new)
                .toList();
    }

    @Override
    public List<FriendResponse> getAllFriendRequestsByUser() {
        return friendRepository
                .findPendingRequests(CurrentUser.id())
                .stream()
                .map(Friend::getRequester)
                .map(userMapper::toUserResponse)
                .map(FriendResponse::new)
                .toList();
    }

    @Override
    public List<FriendResponse> getAllPotentialFriends() {
        return userRepository
                .findAllPotentialFriends(CurrentUser.id())
                .stream()
                .map(userMapper::toUserResponse)
                .map(FriendResponse::new)
                .toList();
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
