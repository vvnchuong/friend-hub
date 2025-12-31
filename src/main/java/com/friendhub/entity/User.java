package com.friendhub.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.friendhub.enums.Gender;
import com.friendhub.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String firstName;
    String lastName;

    @Enumerated(EnumType.STRING)
    Gender gender;

    @Column(unique = true, nullable = false)
    String email;

    @Column(nullable = false)
    String password;

    @Column(columnDefinition = "MEDIUMTEXT")
    String bio;

    String avatarUrl;
    String coverUrl;
    String phoneNumber;
    String address;
    Instant createdAt;
    Instant updatedAt;

    @Enumerated(EnumType.STRING)
    UserStatus status;
    String bannedReason;
    Instant bannedAt;

    @ManyToOne
    @JoinColumn(name = "banned_by")
    @JsonIgnore
    User bannedBy;

    @ManyToOne
    @JoinColumn(name = "role_id")
    Role role;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<PostLike> postLikes = new ArrayList<>();

    // group
    @OneToMany(mappedBy = "user")
    List<GroupJoinRequest> joinRequestsSent = new ArrayList<>();

    @OneToMany(mappedBy = "handledBy")
    List<GroupJoinRequest> joinRequestsHandled = new ArrayList<>();

    // notification
    @OneToMany(mappedBy = "sender")
    List<Notification> notificationsSender = new ArrayList<>();

    @OneToMany(mappedBy = "receiver")
    List<Notification> notificationsReceiver = new ArrayList<>();

}
