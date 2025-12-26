package com.friendhub.service;

import com.friendhub.entity.User;

public interface UserService {

    User createUser(User user);

    User getUserById(long userId);

    void deleteUser(long userId);

}
