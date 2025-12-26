package com.friendhub.service;

import com.friendhub.dto.request.AdminUserCreationRequest;
import com.friendhub.dto.request.AdminUserSearchRequest;
import com.friendhub.dto.request.AdminUserUpdateRequest;
import com.friendhub.dto.response.AdminUserResponse;
import com.friendhub.dto.response.PageResponse;
import com.friendhub.dto.response.UserResponse;
import com.friendhub.entity.Role;
import com.friendhub.entity.User;
import com.friendhub.enums.ErrorCode;
import com.friendhub.exception.AppException;
import com.friendhub.mapper.UserMapper;
import com.friendhub.repository.PostRepository;
import com.friendhub.repository.RoleRepository;
import com.friendhub.repository.UserRepository;
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

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Transactional
    public UserResponse createUser(AdminUserCreationRequest request) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new AppException(ErrorCode.USER_ALREADY_EXISTED);

        User user = userMapper.toUser(request);
        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);

        return userMapper.toUserResponse(userService.createUser(user));
    }

    @Transactional(readOnly = true)
    public PageResponse<AdminUserResponse> getAllUsers(AdminUserSearchRequest request,
                                                       Pageable pageable) {
        Specification<User> spec = UserSpecification.build(request);

        Page<User> page = userRepository.findAll(spec, pageable);

        return new PageResponse<>(
                page.getContent()
                        .stream()
//                        .map(userMapper::toAdminUserResponse)
                        .map(user -> {
                            long totalPosts = postRepository.countByUserId(user.getId());
                            long totalFriends = userRepository.countAllFriendsOfUser(user.getId());
                            AdminUserResponse response = userMapper.toAdminUserResponse(user);
                            response.setTotalPosts(totalPosts);
                            response.setTotalFriends(totalFriends);
                            return response;
                        })
                        .toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }

    @Transactional(readOnly = true)
    public UserResponse getUserDetail(long userId) {
        return userMapper.toUserResponse(userService.getUserById(userId));
    }

    @Transactional
    public UserResponse updateUser(long userId, AdminUserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        userMapper.updateAdminUser(user, request);

        if (request.getRole() != null) {
            Role role = roleRepository.findByName(request.getRole())
                    .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
            user.setRole(role);
        }

        return userMapper.toUserResponse(user);
    }

    @Transactional
    public void deleteUser(long userId) {
        userService.deleteUser(userId);
    }

}
