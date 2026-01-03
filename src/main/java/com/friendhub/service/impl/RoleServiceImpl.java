package com.friendhub.service.impl;

import com.friendhub.entity.Role;
import com.friendhub.enums.ErrorCode;
import com.friendhub.enums.UserRole;
import com.friendhub.exception.AppException;
import com.friendhub.repository.RoleRepository;
import com.friendhub.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getRoleByName(UserRole role) {
        return roleRepository.findByName(role)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
    }

}
