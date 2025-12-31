package com.friendhub.service;

import com.friendhub.dto.request.BanUserRequest;
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
import com.friendhub.repository.RoleRepository;
import com.friendhub.repository.UserRepository;
import com.friendhub.utils.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Transactional
    public UserResponse register(UserCreationRequest request) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new AppException(ErrorCode.USER_ALREADY_EXISTED);

        User user = userMapper.toUser(request);
        Role role = roleRepository.findByName(UserRole.MEMBER)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);

        return userMapper.toUserResponse(userService.createUser(user));
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(long userId) {
        return userMapper.toUserResponse(userService.getUserById(userId));
    }

    @Transactional
    public UserResponse updateMyProfile(UserUpdateRequest request) {
        User user = userService.getUserById(CurrentUser.id());

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

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    }

    @Transactional
    public void lockAccount() {
        if (!userRepository.existsById(CurrentUser.id()))
            throw new AppException(ErrorCode.USER_NOT_FOUND);

        userRepository.lockAccount(CurrentUser.id());
    }

    @Transactional
    public void unLook() {
        if (!userRepository.existsById(CurrentUser.id()))
            throw new AppException(ErrorCode.USER_NOT_FOUND);

        userRepository.unLockAccount(CurrentUser.id());
    }

}
