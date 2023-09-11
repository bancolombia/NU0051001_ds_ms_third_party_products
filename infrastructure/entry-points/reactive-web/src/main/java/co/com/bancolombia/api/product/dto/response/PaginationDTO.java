package co.com.bancolombia.api.product.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * the pagination strategy used is based on pages, where from query params pageNumber and
 * pageSize are expected in the request. In the response, it returns a section of links,
 * to refer to related pages.
 */
@Getter
@Builder(toBuilder = true)
public class PaginationDTO<T> {

    private final Meta meta;
    private final T data;
    private final Links links;

    @Getter
    @Builder(toBuilder = true)
    public static class Links {

        private final String self;
        private final String first;
        private final String prev;
        private final String next;
        private final String last;
    }

    @Getter
    @Builder(toBuilder = true)
    public static class Meta {

        private final Integer totalPage;
    }

}