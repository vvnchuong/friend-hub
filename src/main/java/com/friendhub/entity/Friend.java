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
@Table(name = "friends")
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @ManyToOne
    @JoinColumn(name = "requester_id", referencedColumnName = "id", nullable = false)
    User requester;

    @ManyToOne
    @JoinColumn(name = "addressee_id", referencedColumnName = "id", nullable = false)
    User addressee;

    @Enumerated(EnumType.STRING)
    FriendStatus status;

    Instant createdAt;
    Instant updatedAt;

}
