package com.friendhub.repository;

import com.friendhub.entity.Role;
import com.friendhub.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(ERole roleName);

}
