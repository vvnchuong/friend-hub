package com.friendhub.entity;

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
@Table(name = "post_likes")
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "post_id")
    Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

}
