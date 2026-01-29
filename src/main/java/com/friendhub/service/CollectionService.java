package com.friendhub.service;

import com.friendhub.dto.request.CollectionCreationRequest;
import com.friendhub.dto.request.CollectionUpdateRequest;
import com.friendhub.dto.response.CollectionResponse;
import com.friendhub.dto.response.CursorResponse;

public interface CollectionService {

    CollectionResponse addPostToCollection(long collectionId, long postId);

    void removePostFromCollection(long collectionId, long postId);

    CollectionResponse createCollection(CollectionCreationRequest request);

    CursorResponse<CollectionResponse> getAllCollections(Long lastId);

    CollectionResponse getCollectionById(long collectionId);

    CollectionResponse updateCollection(long collectionId, CollectionUpdateRequest request);

    void deleteCollection(long collectionId);

}
