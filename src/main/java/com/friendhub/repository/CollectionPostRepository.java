package com.friendhub.repository;

import com.friendhub.entity.CollectionPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionPostRepository extends JpaRepository<CollectionPost, Long> {

    boolean existsByCollection_IdAndPostId(long collectionId, long postId);

    void deleteByCollection_IdAndPostId(long collectionId, long postId);

}
