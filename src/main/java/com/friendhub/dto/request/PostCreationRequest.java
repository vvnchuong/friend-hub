package com.friendhub.dto.request;

import com.friendhub.enums.Privacy;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostCreationRequest {

    String content;

    Privacy privacy;

    List<PostMediaCreationRequest> mediaList;

}
