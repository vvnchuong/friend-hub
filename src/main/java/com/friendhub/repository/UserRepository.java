package com.friendhub.repository;

import com.friendhub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsById(long userId);

    Optional<User> findByEmail(String email);

    @Query(value = "SELECT u.* " +
            "FROM users u " +
            "LEFT JOIN friends f " +
            "ON (f.requester_id = u.id AND f.addressee_id = :userId) " +
            "OR (f.addressee_id = u.id AND f.requester_id = :userId) " +
            "WHERE u.id != :userId " +
            "AND (f.id IS NULL " +
            "OR f.status NOT IN ('ACCEPTED', 'PENDING', 'BLOCKED')) ", nativeQuery = true)
    List<User> findAllPotentialFriends(long userId);

}
