package com.friendhub.mapper;

import com.friendhub.dto.request.PostMediaCreationRequest;
import com.friendhub.dto.response.PostMediaResponse;
import com.friendhub.entity.PostMedia;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMediaMapper {

    PostMedia toPostMedia(PostMediaCreationRequest request);

    PostMediaResponse toPostMediaResponse(PostMedia postMedia);

}
