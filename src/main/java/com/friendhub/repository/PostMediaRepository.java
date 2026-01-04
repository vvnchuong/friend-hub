package com.friendhub.repository;

import com.friendhub.entity.PostMedia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;

public interface PostMediaRepository extends JpaRepository<PostMedia, Long> {

    List<PostMedia> findByPostId(long postId);

//    Map<Long, List<PostMedia>> findByPostIdIn(List<Long> postIds);

}
