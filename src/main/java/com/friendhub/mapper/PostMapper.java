package com.friendhub.mapper;

import com.friendhub.dto.request.PostCreationRequest;
import com.friendhub.dto.request.PostUpdateRequest;
import com.friendhub.dto.response.PostResponse;
import com.friendhub.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring",
        uses = UserMapper.class)
public interface PostMapper {

    Post toPost(PostCreationRequest request);

    @Mapping(target = "author", source = "user")
    @Mapping(target = "originalPost.author", source = "originalPost.user")
    PostResponse toPostResponse(Post post);

    void updatePost(@MappingTarget Post post, PostUpdateRequest request);

}
