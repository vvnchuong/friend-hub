package com.friendhub.service;

import com.friendhub.dto.request.FriendAcceptRequest;
import com.friendhub.dto.request.FriendRejectRequest;
import com.friendhub.dto.request.FriendCreationRequest;
import com.friendhub.dto.request.UnFriendRequest;
import com.friendhub.dto.response.CursorResponse;
import com.friendhub.dto.response.FriendResponse;

public interface FriendService {

    void addFriendRequest(FriendCreationRequest request);

    void acceptFriendRequest(FriendAcceptRequest request);

    void rejectFriendRequest(FriendRejectRequest request);

    void unFriend(UnFriendRequest request);

    CursorResponse<FriendResponse> getAllFriends(Long lastId);

    CursorResponse<FriendResponse> getAllFriendRequests(Long lastId);

    CursorResponse<FriendResponse> getAllPotentialFriends(Long lastId);

    boolean areFriends(long userId1, long userId2);

}
