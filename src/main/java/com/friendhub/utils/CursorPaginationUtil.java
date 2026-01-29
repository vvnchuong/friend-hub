package com.friendhub.utils;

import com.friendhub.dto.response.CursorResponse;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class CursorPaginationUtil {

    public static <T, R> CursorResponse<R> execute(
            int pageSize,
            Supplier<List<T>> fetcher,
            Function<T, Long> idExtractor,
            Function<List<T>, List<R>> mapper) {

        List<T> items = fetcher.get();

        boolean hasNext = items.size() > pageSize;

        if (hasNext)
            items = items.subList(0, pageSize);

        Long nextCursor = hasNext
                ? idExtractor.apply(items.getLast())
                : null;

        List<R> data = mapper.apply(items);

        return CursorResponse.<R>builder()
                .data(data)
                .hasNext(hasNext)
                .nextCursor(nextCursor)
                .build();
    }

}
