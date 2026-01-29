package com.friendhub.entity;

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
@Table(name = "collection_posts")
public class CollectionPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_id", nullable = false)
    Collection collection;

    @Column(name = "post_id", nullable = false)
    long postId;

    Instant createdAt;

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
    }

}
