package com.friendhub.entity;

import com.friendhub.enums.ReportReason;
import com.friendhub.enums.ReportStatus;
import com.friendhub.enums.TargetType;
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
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @ManyToOne
    @JoinColumn(name = "reporter_id", nullable = false)
    User reporter;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    TargetType targetType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    ReportReason reportReason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    ReportStatus status;

    long targetId;

    @ManyToOne
    @JoinColumn(name = "handled_id")
    User handledBy;

    Instant createdAt;
    Instant resolvedAt;

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
    }

}
