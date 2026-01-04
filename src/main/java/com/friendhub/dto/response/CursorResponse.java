package com.friendhub.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class    CursorResponse<T> {

    List<T> data;
    Long nextCursor;
    boolean hasNext;

}
