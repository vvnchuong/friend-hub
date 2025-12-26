package com.friendhub.repository;

import com.friendhub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>,
        JpaSpecificationExecutor<User> {

    boolean existsByEmail(String email);

    boolean existsById(long userId);

    Optional<User> findByEmail(String email);

    @Query("""
    select u from User u
    where u.id <> :userId
      and not exists (
          select 1 from Friend f
          where (
              (f.userLow.id = :userId and f.userHigh.id = u.id)
              or
              (f.userHigh.id = :userId and f.userLow.id = u.id)
          )
      )
""")
    List<User> findAllPotentialFriends(long userId);

    @Query(value = "SELECT COUNT(*) " +
            "FROM friends " +
            "WHERE status = 'ACCEPTED' " +
            "AND :userId IN (requester_id, user_high_id, user_low_id)", nativeQuery = true)
    long countAllFriendsOfUser(@Param("userId") long userId);

}
