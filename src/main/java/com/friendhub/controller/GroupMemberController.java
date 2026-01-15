package com.friendhub.controller;

import com.friendhub.dto.request.HandleJoinRequestRequest;
import com.friendhub.dto.request.UpdateMemberRoleRequest;
import com.friendhub.dto.response.ApiResponse;
import com.friendhub.dto.response.CursorResponse;
import com.friendhub.dto.response.GroupJoinRequestResponse;
import com.friendhub.dto.response.GroupMemberResponse;
import com.friendhub.service.UserGroupMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/groups")
public class GroupMemberController {

    private final UserGroupMemberService userGroupMemberService;

    @GetMapping("/{groupId}/members")
    public ApiResponse<CursorResponse<GroupMemberResponse>> getGroupMembers(
            @PathVariable("groupId") long groupId,
            @RequestParam(required = false) Long lastId) {
        return ApiResponse.<CursorResponse<GroupMemberResponse>>builder()
                .message("Group members retrieved successfully.")
                .result(userGroupMemberService.getAllMembers(groupId, lastId))
                .build();
    }

    @DeleteMapping("/{groupId}/members/{memberId}")
    public ApiResponse<Void> removeMemberFromGroup(
            @PathVariable("groupId") long groupId,
            @PathVariable("memberId") long memberId) {
        userGroupMemberService.removeMemberFromGroup(groupId, memberId);
        return ApiResponse.<Void>builder()
                .message("Remove a member from the group.")
                .build();
    }

    @PostMapping("/{groupId}/leave")
    public ApiResponse<Void> leaveGroup(
            @PathVariable("groupId") long groupId) {
        userGroupMemberService.leaveGroup(groupId);
        return ApiResponse.<Void>builder()
                .message("Leave a group successfully.")
                .build();
    }

    @PutMapping("/{groupId}/members/{memberId}")
    public ApiResponse<Void> updateMemberRole(
            @PathVariable("groupId") long groupId,
            @PathVariable("memberId") long memberId,
            @RequestBody UpdateMemberRoleRequest role) {
        userGroupMemberService.updateMemberRole(groupId, memberId, role.getRole());

        return ApiResponse.<Void>builder()
                .message("Update member role successfully.")
                .build();
    }

    @PostMapping("/{groupId}/join")
    public ApiResponse<Void> joinGroup(
            @PathVariable("groupId") long groupId) {
        userGroupMemberService.joinGroup(groupId);
        return ApiResponse.<Void>builder()
                .message("Join successfully.")
                .build();
    }

    @PostMapping("/{groupId}/request")
    public ApiResponse<GroupJoinRequestResponse> requestToJoinGroup(
            @PathVariable("groupId") long groupId) {
        return ApiResponse.<GroupJoinRequestResponse>builder()
                .message("Join request sent successfully.")
                .result(userGroupMemberService.requestToJoinGroup(groupId))
                .build();
    }

    @GetMapping("/{groupId}/pending")
    public ApiResponse<List<GroupJoinRequestResponse>> getPendingJoinRequests(
            @PathVariable("groupId") long groupId) {
        return ApiResponse.<List<GroupJoinRequestResponse>>builder()
                .message("Get pending requests.")
                .result(userGroupMemberService.getPendingJoinRequests(groupId))
                .build();
    }

    @PostMapping("/{groupId}/handle/{requestId}")
    public ApiResponse<Void> handleJoinRequest(
            @PathVariable("groupId") long groupId,
            @PathVariable("requestId") long requestId,
            @RequestBody HandleJoinRequestRequest request) {
        userGroupMemberService.handleJoinRequest(groupId, requestId, request);

        return ApiResponse.<Void>builder()
                .message("Handle join request.")
                .build();
    }

}
