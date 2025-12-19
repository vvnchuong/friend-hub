package com.friendhub.service.impl;

import com.friendhub.dto.request.FriendAcceptRequest;
import com.friendhub.dto.request.FriendCancelRequest;
import com.friendhub.dto.request.FriendCreationRequest;
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
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public void addFriendRequest(FriendCreationRequest request) {
        User requester = getUserById(CurrentUser.id());
        User addressee = getUserById(request.getUserId());

        checkIfAlreadyFriends(requester, addressee);
        checkIfRequestPendingEitherWay(requester, addressee);

        Friend friendRequest = Friend.builder()
                .requester(requester)
                .addressee(addressee)
                .status(FriendStatus.PENDING)
                .createdAt(Instant.now())
                .build();

        friendRepository.save(friendRequest);
    }

    @Override
    public void acceptFriendRequest(FriendAcceptRequest request) {
        User requester = getUserById(request.getUserId());
        User addressee = getUserById(CurrentUser.id());

        Friend friendRequest = friendRepository.findByAddresseeIdAndRequesterIdAndStatus(
                addressee.getId(), requester.getId(), FriendStatus.PENDING)
                    .orElseThrow(() -> new AppException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));

        friendRequest.setStatus(FriendStatus.ACCEPTED);
        friendRequest.setUpdatedAt(Instant.now());

        friendRepository.save(friendRequest);
    }

    @Override
    public void rejectFriendRequest(FriendCancelRequest request) {
        User requester = getUserById(request.getUserId());
        User addressee = getUserById(CurrentUser.id());;

        Friend friendRequest = friendRepository.findByAddresseeIdAndRequesterIdAndStatus(
                        addressee.getId(), requester.getId(), FriendStatus.PENDING)
                .orElseThrow(() -> new AppException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));

        friendRequest.setStatus(FriendStatus.REJECTED);
        friendRequest.setUpdatedAt(Instant.now());

        friendRepository.save(friendRequest);
    }

    @Override
    public void unFriend(UnFriendRequest request) {
        User currentUser = getUserById(CurrentUser.id());
        User otherUser = getUserById(request.getUserId());

        Friend friend = friendRepository
                .findByRequesterIdAndAddresseeIdAndStatus(currentUser.getId(), otherUser.getId(), FriendStatus.ACCEPTED)
                .or(() -> friendRepository.findByRequesterIdAndAddresseeIdAndStatus(otherUser.getId(), currentUser.getId(), FriendStatus.ACCEPTED))
                .orElseThrow(() -> new AppException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));

        friend.setStatus(FriendStatus.UNFRIENDED);
        friend.setUpdatedAt(Instant.now());

        friendRepository.save(friend);
    }

    @Override
    public List<FriendResponse> getAllFriendsByUser() {
        List<Friend> asRequester = friendRepository
                .findByRequesterIdAndStatus(CurrentUser.id(), FriendStatus.ACCEPTED);

        List<Friend> asAddressee = friendRepository
                .findByAddresseeIdAndStatus(CurrentUser.id(), FriendStatus.ACCEPTED);

        return Stream.concat(
                asRequester.stream().map(Friend::getAddressee),
                asAddressee.stream().map(Friend::getRequester)
        )
                .map(userMapper::toUserResponse)
                .map(FriendResponse::new)
                .toList();
    }

    @Override
    public List<FriendResponse> getAllFriendsById(long userId) {
        List<Friend> asRequester = friendRepository
                .findByRequesterIdAndStatus(userId, FriendStatus.ACCEPTED);

        List<Friend> asAddressee = friendRepository
                .findByAddresseeIdAndStatus(userId, FriendStatus.ACCEPTED);

        return Stream.concat(
                        asRequester.stream().map(Friend::getAddressee),
                        asAddressee.stream().map(Friend::getRequester)
                )
                .map(userMapper::toUserResponse)
                .map(FriendResponse::new)
                .toList();
    }

    @Override
    public List<FriendResponse> getAllFriendRequestsByUser() {
        return friendRepository
                .findByAddresseeIdAndStatus(CurrentUser.id(), FriendStatus.PENDING)
                .stream()
                .map(Friend::getRequester)
                .map(userMapper::toUserResponse)
                .map(FriendResponse::new)
                .toList();
    }

    @Override
    public List<FriendResponse> getAllPotentialFriends() {
        return userRepository.findAllPotentialFriends(CurrentUser.id())
                .stream()
                .map(userMapper::toUserResponse)
                .map(FriendResponse::new)
                .toList();
    }

    private User getUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    private void checkIfAlreadyFriends(User user1, User user2) {
        boolean areAlreadyFriends = friendRepository.existsByRequesterIdAndAddresseeIdAndStatus(
                user1.getId(), user2.getId(), FriendStatus.ACCEPTED)
                || friendRepository.existsByRequesterIdAndAddresseeIdAndStatus(
                user2.getId(), user1.getId(), FriendStatus.ACCEPTED);

        if (areAlreadyFriends) {
            throw new AppException(ErrorCode.ALREADY_FRIENDS);
        }
    }

    private void checkIfRequestPendingEitherWay(User user1, User user2) {
        boolean pendingEitherWay =
                friendRepository.existsByRequesterIdAndAddresseeIdAndStatus(
                        user1.getId(), user2.getId(), FriendStatus.PENDING)
                        || friendRepository.existsByRequesterIdAndAddresseeIdAndStatus(
                        user2.getId(), user1.getId(), FriendStatus.PENDING);

        if (pendingEitherWay) {
            throw new AppException(ErrorCode.FRIEND_REQUEST_ALREADY);
        }
    }

}
