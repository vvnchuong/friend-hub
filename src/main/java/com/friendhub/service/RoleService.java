package com.friendhub.service;

import com.friendhub.entity.Role;
import com.friendhub.enums.UserRole;

public interface RoleService {

    Role getRoleByName(UserRole role);

}
