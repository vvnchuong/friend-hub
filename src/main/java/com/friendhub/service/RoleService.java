package com.friendhub.service;

import com.friendhub.entity.Role;
import com.friendhub.enums.UserRole;

public interface RoleService {

    void createRole(Role role);

    Role getRoleByName(UserRole role);

}
