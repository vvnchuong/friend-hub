package com.friendhub.service;

import com.friendhub.dto.request.ChangePasswordRequest;
import com.friendhub.dto.request.UserCreationRequest;
import com.friendhub.dto.request.UserUpdateRequest;
import com.friendhub.dto.response.UserResponse;
import com.friendhub.entity.Role;
import com.friendhub.entity.User;
import com.friendhub.enums.ErrorCode;
import com.friendhub.enums.UserRole;
import com.friendhub.exception.AppException;
import com.friendhub.mapper.UserMapper;
import com.friendhub.utils.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Transactional
    public UserResponse register(UserCreationRequest request) {
        if (userService.isExistedByEmail(request.getEmail()))
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);

        User user = userMapper.toUser(request);
        Role role = roleService.getRoleByName(UserRole.MEMBER);

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);

        return userMapper.toUserResponse(userService.createUser(user));
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(long userId) {
        User user = userService.getUserById(userId);
        userService.assertActive(user);

        return userMapper.toUserResponse(user);
    }

    @Transactional
    public UserResponse updateMyProfile(UserUpdateRequest request) {
        User user = userService.getUserById(CurrentUser.id());

        userService.assertActive(user);

        userMapper.updateUser(user, request);

        return userMapper.toUserResponse(user);
    }

    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        User user = userService.getUserById(CurrentUser.id());

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword()))
            throw new AppException(ErrorCode.INVALID_OLD_PASSWORD);

        if (!request.getNewPassword().equals(request.getConfirmPassword()))
            throw new AppException(ErrorCode.PASSWORD_CONFIRM_MISMATCH);

        userService.changePassword(user,
                passwordEncoder.encode(request.getNewPassword()));
    }

    @Transactional
    public void lockAccount() {
        userService.lockAccount(CurrentUser.id());
    }

    @Transactional
    public void unLockAccount() {
        userService.unLockAccount(CurrentUser.id());
    }

}
