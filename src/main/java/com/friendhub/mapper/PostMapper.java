package com.friendhub.mapper;

import com.friendhub.dto.request.GroupPostCreationRequest;
import com.friendhub.dto.request.PostCreationRequest;
import com.friendhub.dto.request.PostUpdateRequest;
import com.friendhub.dto.response.PostResponse;
import com.friendhub.entity.Post;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface PostMapper {

    Post toPost(PostCreationRequest request);

    @Mapping(target = "author", source = "user")
    @Mapping(target = "author.role", source = "user.role.name")
    PostResponse toPostResponse(Post post);

    void updatePost(@MappingTarget Post post, PostUpdateRequest request);

    @AfterMapping
    default void setCreatedAt(@MappingTarget Post post) {
        if (post.getCreatedAt() == null)
            post.setCreatedAt(Instant.now());
    }

    @AfterMapping
    default void setUpdatedAt(@MappingTarget Post post) {
        post.setUpdatedAt(Instant.now());
    }

}
