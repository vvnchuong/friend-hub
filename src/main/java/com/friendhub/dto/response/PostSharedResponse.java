package com.friendhub.dto.response;

import com.friendhub.enums.Privacy;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostSharedResponse {

    long id;
    String content;
    Privacy privacy;
    Instant createdAt;
    UserBasicInfoResponse author;

}
