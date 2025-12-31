package com.friendhub.entity;

import com.friendhub.enums.CommentPolicy;
import com.friendhub.enums.Privacy;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(columnDefinition = "MEDIUMTEXT")
    String content;

    @Enumerated(EnumType.STRING)
    Privacy privacy;

    @Enumerated(EnumType.STRING)
    CommentPolicy commentPolicy;

    Instant createdAt;
    Instant updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<PostMedia> postMedia = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "group_id")
    Group group;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<Notification> notifications = new ArrayList<>();

}
