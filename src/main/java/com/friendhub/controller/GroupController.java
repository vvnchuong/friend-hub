package com.friendhub.controller;

import com.friendhub.dto.request.GroupCreationRequest;
import com.friendhub.dto.request.GroupUpdateRequest;
import com.friendhub.dto.response.ApiResponse;
import com.friendhub.dto.response.CursorResponse;
import com.friendhub.dto.response.GroupDetailResponse;
import com.friendhub.dto.response.GroupResponse;
import com.friendhub.service.UserGroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/groups")
public class GroupController {

    private final UserGroupService userGroupService;

    @PostMapping
    public ApiResponse<GroupResponse> createGroup(
            @RequestBody @Valid GroupCreationRequest request) {
        return ApiResponse.<GroupResponse>builder()
                .message("Group created successfully.")
                .result(userGroupService.createGroup(request))
                .build();
    }

    @GetMapping("/suggested")
    public ApiResponse<CursorResponse<GroupResponse>> getAllGroupsSuggested(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long lastId) {
        return ApiResponse.<CursorResponse<GroupResponse>>builder()
                .message("Groups retrieved successfully.")
                .result(userGroupService.getSuggestedGroups(keyword, lastId))
                .build();
    }

    @GetMapping("/{groupId}")
    public ApiResponse<GroupDetailResponse> getGroupDetail(
            @PathVariable("groupId") long groupId) {
        return ApiResponse.<GroupDetailResponse>builder()
                .message("Group details retrieved successfully.")
                .result(userGroupService.getGroupDetail(groupId))
                .build();
    }

    @GetMapping("/my")
    public ApiResponse<CursorResponse<GroupResponse>> getMyGroups(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long lastId) {
        return ApiResponse.<CursorResponse<GroupResponse>>builder()
                .message("Groups retrieved successfully.")
                .result(userGroupService.getMyGroups(keyword, lastId))
                .build();
    }

    @PutMapping("/{groupId}")
    public ApiResponse<GroupResponse> updateGroup(
            @PathVariable("groupId") long groupId,
            @RequestBody @Valid GroupUpdateRequest request) {
        return ApiResponse.<GroupResponse>builder()
                .message("Group updated successfully.")
                .result(userGroupService.updateGroup(groupId, request))
                .build();
    }

    @DeleteMapping("/{groupId}")
    public ApiResponse<Void> deleteGroup(
            @PathVariable("groupId") long groupId) {
        userGroupService.deleteGroup(groupId);
        return ApiResponse.<Void>builder()
                .message("Group deleted successfully.")
                .build();
    }


}
