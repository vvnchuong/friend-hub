package com.friendhub.controller;

import com.friendhub.dto.request.GroupSearchRequest;
import com.friendhub.dto.request.GroupUpdateRequest;
import com.friendhub.dto.response.ApiResponse;
import com.friendhub.dto.response.GroupDetailResponse;
import com.friendhub.dto.response.GroupResponse;
import com.friendhub.dto.response.PageResponse;
import com.friendhub.service.AdminGroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/groups")
public class AdminGroupController {

    private final AdminGroupService adminGroupService;

    @GetMapping
    public ApiResponse<PageResponse<GroupResponse>> getAllGroups(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 5) Pageable pageable) {
        GroupSearchRequest request = GroupSearchRequest.builder()
                .keyword(keyword)
                .build();

        return ApiResponse.<PageResponse<GroupResponse>>builder()
                .message("Groups retrieved successfully.")
                .result(adminGroupService.getAllGroups(request, pageable))
                .build();
    }

    @GetMapping("/{groupId}")
    public ApiResponse<GroupDetailResponse> getGroupDetail(
            @PathVariable("groupId") long groupId) {
        return ApiResponse.<GroupDetailResponse>builder()
                .message("Group details retrieved successfully.")
                .result(adminGroupService.getGroupDetail(groupId))
                .build();
    }

    @PutMapping("/{groupId}")
    public ApiResponse<GroupResponse> updateGroup(
            @PathVariable("groupId") long groupId,
            @RequestBody @Valid GroupUpdateRequest request) {
        return ApiResponse.<GroupResponse>builder()
                .message("Group updated successfully.")
                .result(adminGroupService.updateGroup(groupId, request))
                .build();
    }

    @DeleteMapping("/{groupId}")
    public ApiResponse<Void> deleteGroup(
            @PathVariable("groupId") long groupId) {
        adminGroupService.deleteGroup(groupId);
        return ApiResponse.<Void>builder()
                .message("Group deleted successfully.")
                .build();
    }

}
