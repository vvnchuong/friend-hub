package com.friendhub.service;

import com.friendhub.dto.request.FriendAcceptRequest;
import com.friendhub.dto.request.FriendRejectRequest;
import com.friendhub.dto.request.FriendCreationRequest;
import com.friendhub.dto.request.UnFriendRequest;
import com.friendhub.dto.response.FriendResponse;

import java.util.List;

public interface FriendService {

    void addFriendRequest(FriendCreationRequest request);

    void acceptFriendRequest(FriendAcceptRequest request);

    void rejectFriendRequest(FriendRejectRequest request);

    void unFriend(UnFriendRequest request);

    List<FriendResponse> getAllFriendsByUser();

    List<FriendResponse> getAllFriendRequestsByUser();

    List<FriendResponse> getAllPotentialFriends();

    boolean areFriends(long userId1, long userId2);

}
