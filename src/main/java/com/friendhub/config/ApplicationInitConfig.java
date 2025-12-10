package com.friendhub.config;

import com.friendhub.entity.Role;
import com.friendhub.entity.User;
import com.friendhub.enums.ERole;
import com.friendhub.enums.ErrorCode;
import com.friendhub.exception.AppException;
import com.friendhub.repository.RoleRepository;
import com.friendhub.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationInitConfig {

    private final PasswordEncoder passwordEncoder;

    public ApplicationInitConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public ApplicationRunner applicationRunner(UserRepository userRepository,
                                               RoleRepository roleRepository) {
        return args -> {
            if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {

                Role role = roleRepository.findByName(ERole.ADMIN)
                        .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

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
