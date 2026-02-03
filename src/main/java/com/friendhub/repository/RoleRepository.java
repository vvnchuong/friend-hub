package com.friendhub.repository;

import com.friendhub.entity.Role;
import com.friendhub.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(UserRole roleName);

    boolean existsByName(UserRole name);

}
