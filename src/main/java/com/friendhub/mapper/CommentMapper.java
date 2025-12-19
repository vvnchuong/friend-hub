package com.friendhub.mapper;

import com.friendhub.dto.request.CommentCreationRequest;
import com.friendhub.dto.response.CommentResponse;
import com.friendhub.entity.Comment;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.Instant;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment toComment(CommentCreationRequest request);

    @Mapping(target = "author", source = "user")
    @Mapping(target = "author.role", source = "user.role.name")
    CommentResponse toCommentResponse(Comment comment);

    @AfterMapping
    default void setCreatedAt(@MappingTarget Comment comment) {
        if (comment.getCreatedAt() == null)
            comment.setCreatedAt(Instant.now());
    }

}
