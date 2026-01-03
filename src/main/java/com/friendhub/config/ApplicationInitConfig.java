package com.friendhub.config;

import com.friendhub.entity.Role;
import com.friendhub.entity.User;
import com.friendhub.enums.UserRole;
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
            if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {

                Role role = roleService.getRoleByName(UserRole.ADMIN);

                User user = User.builder()
                        .firstName("admin")
                        .lastName("")
                        .email("admin@gmail.com")
                        .password(passwordEncoder.encode("123456"))
                        .role(role)
                        .build();

                userRepository.save(user);
            }
        };
    }

}
