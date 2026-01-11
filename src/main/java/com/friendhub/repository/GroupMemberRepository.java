package com.friendhub.repository;

import com.friendhub.entity.GroupMember;
import com.friendhub.entity.GroupMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupMemberRepository extends JpaRepository<GroupMember, GroupMemberId> {

    @Query(value = "SELECT gm.* " +
            "FROM group_members gm " +
            "WHERE gm.group_id = :groupId " +
            "AND (:lastId IS NULL OR gm.user_id > :lastId) " +
            "ORDER BY gm.user_id ASC " +
            "LIMIT :limit", nativeQuery = true)
    List<GroupMember> findAllMembersInGroup(
            @Param("groupId") long groupId,
            @Param("lastId") Long lastId,
            @Param("limit") int limit);

    int countByGroupId(long groupId);

}
