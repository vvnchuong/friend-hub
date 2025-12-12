package com.friendhub.entity;

import com.friendhub.enums.MediaType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "post_media")
public class PostMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String mediaUrl;

    @Enumerated(EnumType.STRING)
    MediaType type;

    Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "post_id")
    Post post;

}
