package com.friendhub.service;

import com.friendhub.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface UserService {

    User createUser(User user);

    User getUserById(long userId);

    Page<User> getAllUsers(Specification<User> spec, Pageable pageable);

    void deleteUser(long userId);

    void changePassword(User user, String encodedPassword);

    void banUser(long userId, String reason, long handler);

    void unBanUser(long userId);

    void lockAccount(long currentUserId);

    void unLockAccount(long currentUserId);

    void assertActive(User user);

    boolean isExistedByEmail(String email);

    boolean isExistedById(long userId);

    Long countAllFriendsOfUser(long userId);

}
