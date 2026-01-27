package com.friendhub.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageResponse<T> {

    List<T> data;
    int page;
    int size;
    long totalElements;
    int totalPages;
    boolean first;
    boolean last;

}
