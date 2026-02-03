package com.friendhub.controller;

import com.friendhub.dto.request.FriendCreationRequest;
import com.friendhub.dto.response.ApiResponse;
import com.friendhub.dto.response.CursorResponse;
import com.friendhub.dto.response.FriendResponse;
import com.friendhub.service.FriendService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friends")
public class FriendController {

    private final FriendService friendService;

    @PostMapping("/requests")
    public ApiResponse<String> addFriend(
            @RequestBody @Valid FriendCreationRequest request) {
        friendService.addFriendRequest(request);
        return ApiResponse.<String>builder()
                .message("Friend request sent successfully.")
                .build();
    }

    @PostMapping("/{userId}/accept")
    public ApiResponse<Void> acceptFriendRequest(
            @PathVariable("userId") long userId) {
        friendService.acceptFriendRequest(userId);
        return ApiResponse.<Void>builder()
                .message("Friend request accepted successfully.")
                .build();
    }

    @PostMapping("/{userId}/reject")
    public ApiResponse<Void> rejectFriendRequest(
            @PathVariable("userId") long userId) {
        friendService.rejectFriendRequest(userId);
        return ApiResponse.<Void>builder()
                .message("Friend request canceled successfully.")
                .build();
    }

    @PostMapping("/{userId}/unfriend")
    public ApiResponse<String> unFriend(
            @PathVariable("userId") long userId) {
        friendService.unFriend(userId);
        return ApiResponse.<String>builder()
                .message("User unfriended successfully.")
                .build();
    }

    @GetMapping
    public ApiResponse<CursorResponse<FriendResponse>> getAllFriendsByUser(
            @RequestParam(required = false) Long lastId) {
        return ApiResponse.<CursorResponse<FriendResponse>>builder()
                .result(friendService.getAllFriends(lastId))
                .message("Friend request canceled successfully.")
                .build();
    }

    @GetMapping("/requests")
    public ApiResponse<CursorResponse<FriendResponse>> getAllFriendRequests(
            @RequestParam(required = false) Long lastId) {
        return ApiResponse.<CursorResponse<FriendResponse>>builder()
                .message("Friends retrieved successfully.")
                .result(friendService.getAllFriendRequests(lastId))
                .build();
    }

    @GetMapping("/potential")
    public ApiResponse<CursorResponse<FriendResponse>> getAllPotentialFriends(
            @RequestParam(required = false) Long lastId) {
        return ApiResponse.<CursorResponse<FriendResponse>>builder()
                .message("Friends retrieved successfully.")
                .result(friendService.getAllPotentialFriends(lastId))
                .build();
    }

}
