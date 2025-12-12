package com.friendhub.dto.response;

import com.friendhub.enums.MediaType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostMediaResponse {

    long id;
    String mediaUrl;
    MediaType type;
    Instant createdAt;

}
