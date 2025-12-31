package com.friendhub.entity;

import com.friendhub.enums.GroupRole;
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
@Table(name = "group_members")
public class GroupMember {

    @EmbeddedId
    GroupMemberId id = new GroupMemberId();

    @Enumerated(EnumType.STRING)
    GroupRole role;

    Instant joinedAt;

    @ManyToOne
    @MapsId("groupId")
    @JoinColumn(name = "group_id")
    Group group;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    User user;

}
