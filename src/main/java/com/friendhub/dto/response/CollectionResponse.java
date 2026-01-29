package com.friendhub.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CollectionResponse {

    long id;
    String name;
    UserBasicInfoResponse user;
    List<CollectionPostResponse> savedPosts;
    Instant createdAt;

}
