package com.friendhub.service.impl;

import com.friendhub.entity.User;
import com.friendhub.enums.ErrorCode;
import com.friendhub.enums.UserStatus;
import com.friendhub.exception.AppException;
import com.friendhub.repository.UserRepository;
import com.friendhub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User createUser(User user){
        return userRepository.save(user);
    }

    @Override
    public User getUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public Page<User> getAllUsers(Specification<User> spec,
                                  Pageable pageable) {
        return userRepository.findAll(spec, pageable);
    }

    @Override
    public void deleteUser(long userId) {
        boolean isExisted = userRepository.existsById(userId);
        if (!isExisted)
            throw new AppException(ErrorCode.USER_NOT_FOUND);

        userRepository.deleteById(userId);
    }

    @Override
    public void changePassword(User user, String encodedPassword) {
        assertActive(user);
        user.setPassword(encodedPassword);
    }

    @Override
    public void updateStatusUser(long userId, String status) {
        if (!isExistedById(userId))
            throw new AppException(ErrorCode.USER_NOT_FOUND);

        userRepository.setUserStatus(userId, status);
    }

    @Override
    public void assertActive(User user) {
        if (user.getStatus() == null ||
                user.getStatus() != UserStatus.ACTIVE)
            throw new AppException(ErrorCode.USER_DEACTIVATED);
    }

    @Override
    public boolean isExistedByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean isExistedById(long userId) {
        return userRepository.existsById(userId);
    }

    @Override
    public Long countAllFriendsOfUser(long userId) {
        return userRepository.countAllFriendsOfUser(userId);
    }

}
