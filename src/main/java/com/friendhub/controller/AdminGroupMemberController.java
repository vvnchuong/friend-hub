package com.friendhub.controller;

import com.friendhub.dto.response.ApiResponse;
import com.friendhub.dto.response.CursorResponse;
import com.friendhub.dto.response.GroupMemberResponse;
import com.friendhub.service.AdminGroupMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/groups")
public class AdminGroupMemberController {

    private final AdminGroupMemberService adminGroupMemberService;

    @GetMapping("/{groupId}/members")
    public ApiResponse<CursorResponse<GroupMemberResponse>> getGroupMembers(
            @PathVariable("groupId") long groupId,
            @RequestParam(required = false) Long lastId) {
        return ApiResponse.<CursorResponse<GroupMemberResponse>>builder()
                .message("Group members retrieved successfully.")
                .result(adminGroupMemberService.getAllMembers(groupId, lastId))
                .build();
    }

    @DeleteMapping("/{groupId}/members/{memberId}")
    public ApiResponse<Void> removeMemberFromGroup(
            @PathVariable("groupId") long groupId,
            @PathVariable("memberId") long memberId) {
        adminGroupMemberService.removeMemberFromGroup(groupId, memberId);
        return ApiResponse.<Void>builder()
                .message("Remove a member from the group.")
                .build();
    }

}
