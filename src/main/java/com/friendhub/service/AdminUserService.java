package com.friendhub.service;

import com.friendhub.dto.request.AdminUserCreationRequest;
import com.friendhub.dto.request.AdminUserSearchRequest;
import com.friendhub.dto.request.AdminUserUpdateRequest;
import com.friendhub.dto.request.UserUpdateStatusRequest;
import com.friendhub.dto.response.AdminUserResponse;
import com.friendhub.dto.response.PageResponse;
import com.friendhub.dto.response.UserResponse;
import com.friendhub.entity.Role;
import com.friendhub.entity.User;
import com.friendhub.enums.ErrorCode;
import com.friendhub.enums.UserStatus;
import com.friendhub.exception.AppException;
import com.friendhub.mapper.UserMapper;
import com.friendhub.repository.specification.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserService userService;
    private final PostService postService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Transactional
    public UserResponse createUser(AdminUserCreationRequest request) {
        if (userService.isExistedByEmail(request.getEmail()))
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);

        User user = userMapper.toUser(request);
        Role role = roleService.getRoleByName(request.getRole());

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        user.setStatus(UserStatus.ACTIVE);

        return userMapper.toUserResponse(userService.createUser(user));
    }

    @Transactional(readOnly = true)
    public PageResponse<AdminUserResponse> getAllUsers(AdminUserSearchRequest request,
                                                       Pageable pageable) {

        Specification<User> spec = UserSpecification.build(request);

        Page<User> page = userService.getAllUsers(spec, pageable);

        return PageResponse.<AdminUserResponse>builder()
                .data(page.getContent().stream()
                        .map(user -> {
                            Long totalPosts = postService.countTotalPostsOfUser(user.getId());
                            Long totalFriends = userService.countAllFriendsOfUser(user.getId());
                            AdminUserResponse response = userMapper.toAdminUserResponse(user);
                            response.setTotalPosts(totalPosts);
                            response.setTotalFriends(totalFriends);
                            return response;
                        }).toList())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    @Transactional(readOnly = true)
    public UserResponse getUserDetail(long userId) {
        return userMapper.toUserResponse(userService.getUserById(userId));
    }

    @Transactional
    public UserResponse updateUser(long userId, AdminUserUpdateRequest request) {
        User user = userService.getUserById(userId);

        userService.assertActive(user);

        userMapper.updateAdminUser(user, request);

        if (request.getRole() != null) {
            Role role = roleService.getRoleByName(request.getRole());
            user.setRole(role);
        }

        return userMapper.toUserResponse(user);
    }

    @Transactional
    public void deleteUser(long userId) {
        userService.deleteUser(userId);
    }

    @Transactional
    public void updateUserStatus(long userId, UserUpdateStatusRequest request) {
        userService.updateStatusUser(userId, request.getStatus().name());
    }

}
