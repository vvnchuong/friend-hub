package com.friendhub.repository;

import com.friendhub.entity.Group;
import com.friendhub.enums.GroupStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long>,
        JpaSpecificationExecutor<Group> {

    @Query(value = "SELECT g.* " +
            "FROM user_groups g " +
            "LEFT JOIN group_members gm " +
            "ON g.id = gm.group_id " +
            "AND gm.user_id = :userId " +
            "WHERE gm.user_id IS NULL " +
            "AND g.status = 'ACTIVE' " +
            "AND (:lastId IS NULL OR g.id > :lastId) " +
            "AND (:keyword IS NULL " +
            "OR LOWER(g.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(g.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "ORDER BY g.id ASC " +
            "LIMIT :limit", nativeQuery = true)
    List<Group> findSuggestedGroups(
            @Param("userId") long userId,
            @Param("keyword") String keyword,
            @Param("lastId") Long lastId,
            @Param("limit") int limit
    );

    @Query(value = "SELECT g.* " +
            "FROM user_groups g " +
            "JOIN group_members gm " +
            "ON g.id = gm.group_id " +
            "WHERE gm.user_id = :userId " +
            "AND g.status = 'ACTIVE' " +
            "AND (:lastId IS NULL OR g.id > :lastId) " +
            "AND (:keyword IS NULL " +
            "OR LOWER(g.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(g.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "ORDER BY g.id ASC " +
            "LIMIT :limit", nativeQuery = true)
    List<Group> findMyGroups(@Param("userId") long userId,
                             @Param("keyword") String keyword,
                             @Param("lastId") Long lastId,
                             @Param("limit") int limit);

    @Modifying
    @Query(value = "UPDATE groups " +
            "SET status = :status, " +
            "banned_at = NOW() " +
            "WHERE id = :groupId", nativeQuery = true)
    void setGroupStatus(@Param("groupId") long groupId,
                        @Param("status") String status);

}
