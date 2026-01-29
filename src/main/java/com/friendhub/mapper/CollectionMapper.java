package com.friendhub.mapper;

import com.friendhub.dto.request.CollectionCreationRequest;
import com.friendhub.dto.request.CollectionUpdateRequest;
import com.friendhub.dto.response.CollectionResponse;
import com.friendhub.entity.Collection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CollectionMapper {

    Collection toCollection(CollectionCreationRequest request);

    @Mapping(target = "savedPosts", source = "collectionPosts")
    CollectionResponse toCollectionResponse(Collection collection);

    void updateCollection(@MappingTarget Collection collection, CollectionUpdateRequest request);

}
