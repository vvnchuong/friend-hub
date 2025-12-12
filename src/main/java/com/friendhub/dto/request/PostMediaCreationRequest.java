package com.friendhub.dto.request;

import com.friendhub.enums.MediaType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostMediaCreationRequest {

    String mediaUrl;
    MediaType type;

}
