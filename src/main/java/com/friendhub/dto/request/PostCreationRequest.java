package com.friendhub.dto.request;

import com.friendhub.enums.Privacy;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Content is required")
    String content;

    Privacy privacy;

    List<PostMediaCreationRequest> mediaList;

}
