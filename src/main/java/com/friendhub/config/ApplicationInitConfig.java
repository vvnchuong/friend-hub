package com.friendhub.config;

import com.friendhub.entity.Role;
import com.friendhub.entity.User;
import com.friendhub.enums.UserRole;
import com.friendhub.enums.UserStatus;
import com.friendhub.repository.UserRepository;
import com.friendhub.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationInitConfig {

    private final PasswordEncoder passwordEncoder;

    @Bean
    public ApplicationRunner applicationRunner(UserRepository userRepository,
                                               RoleService roleService) {
        return args -> {

            if (!roleService.isExistedByName(UserRole.ADMIN)) {
                Role roleAdmin = new Role();
                roleAdmin.setName(UserRole.ADMIN);
                roleService.createRole(roleAdmin);
            }

            if (!roleService.isExistedByName(UserRole.MEMBER)) {
                Role roleMember = new Role();
                roleMember.setName(UserRole.MEMBER);
                roleService.createRole(roleMember);
            }

            if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {

                Role roleAdmin = roleService.getRoleByName(UserRole.ADMIN);

                User user = User.builder()
                        .firstName("admin")
                        .lastName("")
                        .email("admin@gmail.com")
                        .password(passwordEncoder.encode("123456"))
                        .role(roleAdmin)
                        .status(UserStatus.ACTIVE)
                        .build();

                userRepository.save(user);
            }
        };
    }

}
