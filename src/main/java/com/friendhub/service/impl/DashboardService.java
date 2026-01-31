package com.friendhub.service.impl;

import com.friendhub.dto.response.DashboardSummaryResponse;
import com.friendhub.repository.GroupRepository;
import com.friendhub.repository.PostRepository;
import com.friendhub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final GroupRepository groupRepository;

    public DashboardSummaryResponse summaryResponse() {
        long totalUsers = userRepository.count();
        long totalPosts = postRepository.count();
        long totalGroups = groupRepository.count();

        return DashboardSummaryResponse.builder()
                .totalUsers(totalUsers)
                .totalPosts(totalPosts)
                .totalGroups(totalGroups)
                .build();
    }


}
