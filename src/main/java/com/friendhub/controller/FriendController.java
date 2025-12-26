package com.friendhub.controller;

import com.friendhub.dto.request.FriendAcceptRequest;
import com.friendhub.dto.request.FriendRejectRequest;
import com.friendhub.dto.request.FriendCreationRequest;
import com.friendhub.dto.request.UnFriendRequest;
import com.friendhub.dto.response.ApiResponse;
import com.friendhub.dto.response.FriendResponse;
import com.friendhub.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friends")
public class FriendController {

    private final FriendService friendService;

    @PostMapping("/requests")
    public ApiResponse<String> addFriend(@RequestBody FriendCreationRequest request) {
        friendService.addFriendRequest(request);

        return ApiResponse.<String>builder()
                .message("Friend request sent successfully.")
                .build();
    }

    @PostMapping("/accept")
    public ApiResponse<String> acceptFriendRequest(@RequestBody FriendAcceptRequest request) {
        friendService.acceptFriendRequest(request);

        return ApiResponse.<String>builder()
                .message("Friend request accepted successfully.")
                .build();
    }

    @PostMapping("/reject")
    public ApiResponse<String> rejectFriendRequest(@RequestBody FriendRejectRequest request) {
        friendService.rejectFriendRequest(request);

        return ApiResponse.<String>builder()
                .message("Friend request canceled successfully.")
                .build();
    }

    @PostMapping("/unfriend")
    public ApiResponse<String> unFriend(@RequestBody UnFriendRequest request) {
        friendService.unFriend(request);

        return ApiResponse.<String>builder()
                .message("Unfriended successfully.")
                .build();
    }

    @GetMapping
    public ApiResponse<List<FriendResponse>> getAllFriendsByUser() {
        return ApiResponse.<List<FriendResponse>>builder()
                .result(friendService.getAllFriendsByUser())
                .message("Friend request canceled successfully.")
                .build();
    }

    @GetMapping("/requests")
    public ApiResponse<List<FriendResponse>> getAllFriendRequests() {
        return ApiResponse.<List<FriendResponse>>builder()
                .message("Friends retrieved successfully.")
                .result(friendService.getAllFriendRequestsByUser())
                .build();
    }

    @GetMapping("/potential")
    public ApiResponse<List<FriendResponse>> getAllPotentialFriends() {
        return ApiResponse.<List<FriendResponse>>builder()
                .message("Friends retrieved successfully.")
                .result(friendService.getAllPotentialFriends())
                .build();
    }

}
