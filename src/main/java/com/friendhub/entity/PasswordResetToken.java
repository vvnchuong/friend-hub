package com.friendhub.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "password_reset_tokens")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true)
    String token;

    @OneToOne(fetch = FetchType.LAZY)
    User user;

    Instant expiryTime;

}
