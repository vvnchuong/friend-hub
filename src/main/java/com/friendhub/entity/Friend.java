package com.friendhub.entity;

import com.friendhub.enums.FriendStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(
        name = "friends",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"user_low_id", "user_high_id"}
        )
)
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @ManyToOne
    @JoinColumn(name = "user_low_id", nullable = false)
    User userLow;

    @ManyToOne
    @JoinColumn(name = "user_high_id", nullable = false)
    User userHigh;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    User requester;

    @Enumerated(EnumType.STRING)
    FriendStatus status;

    Instant createdAt;
    Instant updatedAt;

}
