package com.friendhub.repository;

import com.friendhub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>,
        JpaSpecificationExecutor<User> {

    boolean existsByEmail(String email);

    boolean existsById(long userId);

    Optional<User> findByEmail(String email);

    @Query(value = "SELECT * " +
        "FROM users u " +
        "WHERE u.id <> :userId " +
        "AND NOT EXISTS (" +
        "SELECT 1 " +
        "FROM friends f " +
        "WHERE ((f.user_low_id = :userId AND f.user_high_id = u.id) " +
        "OR (f.user_high_id = :userId AND f.user_low_id = u.id)))", nativeQuery = true)
    List<User> findAllPotentialFriends(long userId);

    @Query(value = "SELECT COUNT(*) " +
            "FROM friends " +
            "WHERE status = 'ACCEPTED' " +
            "AND :userId IN (requester_id, user_high_id, user_low_id)", nativeQuery = true)
    Long countAllFriendsOfUser(@Param("userId") long userId);

    @Modifying
    @Query(value = "UPDATE users " +
            "SET status = 'LOOKED' " +
            "WHERE id = :userId", nativeQuery = true)
    void lockAccount(@Param("userId") long userId);

    @Modifying
    @Query(value = "UPDATE users " +
            "SET status = 'ACTIVE' " +
            "WHERE id = :userId", nativeQuery = true)
    void unLockAccount(@Param("userId") long userId);

    @Modifying
    @Query(value = "UPDATE users " +
            "SET status = 'BANNED'," +
            "banned_reason = :reason," +
            "banned_by = :banner, " +
            "banned_at = NOW() " +
            "WHERE id = :userId", nativeQuery = true)
    void banUser(@Param("userId") long userId,
                 @Param("reason") String reason,
                 @Param("banner") long banner);

    @Modifying
    @Query(value = "UPDATE users " +
            "SET status = 'ACTIVE', " +
            "banned_reason = NULL, " +
            "banned_by = NULL, " +
            "banned_at = NULL " +
            "WHERE id = :userId", nativeQuery = true)
    void unBanUser(@Param("userId") long userId);

}
