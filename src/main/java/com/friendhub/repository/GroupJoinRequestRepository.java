package com.friendhub.repository;

import com.friendhub.entity.GroupJoinRequest;
import com.friendhub.enums.JoinRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupJoinRequestRepository extends JpaRepository<GroupJoinRequest, Long> {

    GroupJoinRequest findByUserIdAndGroupId(long userId, long groupId);

    boolean existsByGroupIdAndUserIdAndStatus(long groupId, long userId, JoinRequestStatus status);

    List<GroupJoinRequest> findByGroupIdAndStatus(long groupId, JoinRequestStatus status);

}
