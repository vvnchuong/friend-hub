package com.friendhub.service;

import com.friendhub.entity.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface GroupService {

    Group createGroup(Group group);

    Page<Group> getAllGroups(Specification<Group> spec, Pageable pageable);

    List<Group> getSuggestedGroups(long userId, String keyword, Long lastId, int limit);

    Group getGroupById(long groupId);

    List<Group> getMyGroups(long userId, String keyword, Long lastId, int limit);

    Group updateGroup(Group group);

    void deleteGroup(long groupId);

    boolean isExistedById(long groupId);

}
