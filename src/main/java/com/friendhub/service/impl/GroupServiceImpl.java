package com.friendhub.service.impl;

import com.friendhub.entity.*;
import com.friendhub.enums.ErrorCode;
import com.friendhub.exception.AppException;
import com.friendhub.repository.*;
import com.friendhub.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;

    @Override
    public Group createGroup(Group group) {
        return groupRepository.save(group);
    }

    @Override
    public Page<Group> getAllGroups(Specification<Group> spec, Pageable pageable) {
        return groupRepository.findAll(spec, pageable);
    }

    @Override
    public List<Group> getSuggestedGroups(
            long userId, String keyword, Long lastId, int limit) {
        return groupRepository.findSuggestedGroups(
                userId, keyword, lastId, limit);
    }

    @Override
    public Group getGroupById(long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new AppException(ErrorCode.GROUP_ACCESS_DENIED));
    }

    @Override
    public List<Group> getMyGroups(
            long userId, String keyword, Long lastId, int limit) {
        return groupRepository.findMyGroups(userId, keyword, lastId, limit);
    }

    @Override
    public Group updateGroup(Group group) {
        return groupRepository.save(group);
    }

    @Override
    public void deleteGroup(long groupId) {
        groupRepository.deleteById(groupId);
    }

    @Override
    public boolean isExistedById(long groupId) {
        return groupRepository.existsById(groupId);
    }

}
