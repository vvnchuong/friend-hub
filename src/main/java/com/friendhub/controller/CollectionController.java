package com.friendhub.controller;

import com.friendhub.dto.request.CollectionCreationRequest;
import com.friendhub.dto.request.CollectionUpdateRequest;
import com.friendhub.dto.response.ApiResponse;
import com.friendhub.dto.response.CollectionResponse;
import com.friendhub.dto.response.CursorResponse;
import com.friendhub.service.CollectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/collections")
public class CollectionController {

    private final CollectionService collectionService;

    @PostMapping
    public ApiResponse<CollectionResponse> createCollection(
            @RequestBody @Valid CollectionCreationRequest request) {
        return ApiResponse.<CollectionResponse>builder()
                .message("Collection created successfully.")
                .result(collectionService.createCollection(request))
                .build();
    }

    @PostMapping("/{collectionId}/posts/{postId}")
    public ApiResponse<CollectionResponse> addPostToCollection(
            @PathVariable("collectionId") long collectionId,
            @PathVariable("postId") long postId) {
        return ApiResponse.<CollectionResponse>builder()
                .message("Post added to collection successfully.")
                .result(collectionService
                        .addPostToCollection(collectionId, postId))
                .build();
    }

    @DeleteMapping("/{collectionId}/posts/{postId}")
    public ApiResponse<Void> removePostFromCollection(
            @PathVariable("collectionId") long collectionId,
            @PathVariable("postId") long postId) {
        collectionService.removePostFromCollection(collectionId, postId);
        return ApiResponse.<Void>builder()
                .message("Post removed from collection successfully.")
                .build();
    }

    @GetMapping
    public ApiResponse<CursorResponse<CollectionResponse>> getAllCollections(
            @RequestParam(required = false) Long lastId) {
        return ApiResponse.<CursorResponse<CollectionResponse>>builder()
                .message("Collections retrieved successfully.")
                .result(collectionService.getAllCollections(lastId))
                .build();
    }

    @GetMapping("/{collectionId}")
    public ApiResponse<CollectionResponse> getCollectionById(
            @PathVariable("collectionId") long collectionId) {
        return ApiResponse.<CollectionResponse>builder()
                .message("Collection retrieved successfully.")
                .result(collectionService.getCollectionById(collectionId))
                .build();
    }

    @PutMapping("/{collectionId}")
    public ApiResponse<CollectionResponse> updateCollection(
            @PathVariable("collectionId") long collectionId,
            @RequestBody @Valid CollectionUpdateRequest request) {
        return ApiResponse.<CollectionResponse>builder()
                .message("Collection updated successfully")
                .result(collectionService.updateCollection(collectionId, request))
                .build();
    }

    @DeleteMapping("/{collectionId}")
    public ApiResponse<Void> deleteCollection(
            @PathVariable("collectionId") long collectionId) {
        collectionService.deleteCollection(collectionId);
        return ApiResponse.<Void>builder()
                .message("Collection deleted successfully.")
                .build();
    }

}
