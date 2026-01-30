package com.friendhub.service;

import com.friendhub.entity.PasswordResetToken;
import com.friendhub.entity.User;
import com.friendhub.enums.ErrorCode;
import com.friendhub.exception.AppException;
import com.friendhub.repository.PasswordResetTokenRepository;
import com.friendhub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HexFormat;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Value("${app.reset-password.expire-minutes}")
    private long expireMinutes;

    @Transactional
    public void forgotPassword(String email) {

        userRepository.findByEmail(email).ifPresent(user -> {
            tokenRepository.deleteByUser(user);

            String rawToken = UUID.randomUUID().toString();

            log.info(rawToken);

            String hashed = hash(rawToken);

            PasswordResetToken token = PasswordResetToken.builder()
                    .token(hashed)
                    .user(user)
                    .expiryTime(
                            Instant.now().plus(expireMinutes, ChronoUnit.MINUTES)
                    )
                    .build();

            tokenRepository.save(token);

            String link =
                    frontendUrl + "/reset-password?token=" + rawToken;

            mailService.sendResetPasswordEmail(
                    user.getEmail(),
                    link
            );
        });
    }

    @Transactional
    public void resetPassword(
            String token, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword))
            throw new AppException(ErrorCode.PASSWORD_CONFIRM_MISMATCH);

        String hashed = hash(token);

        PasswordResetToken resetToken =
                tokenRepository.findByToken(hashed)
                        .orElseThrow(() ->
                                new AppException(ErrorCode.INVALID_RESET_TOKEN));

        if (resetToken.getExpiryTime().isBefore(Instant.now()))
            throw new AppException(ErrorCode.RESET_TOKEN_EXPIRED);

        User user = resetToken.getUser();

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        tokenRepository.delete(resetToken);
    }

    private String hash(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
