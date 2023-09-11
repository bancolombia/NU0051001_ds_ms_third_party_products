package co.com.bancolombia.api.product.builder;

import co.com.bancolombia.api.product.dto.response.PaginationDTO;
import co.com.bancolombia.api.product.dto.response.PaginationDTO.Links;
import co.com.bancolombia.api.product.dto.response.PaginationDTO.Meta;
import co.com.bancolombia.api.product.dto.response.ProductSearchResponseDTO;
import co.com.bancolombia.api.product.mapper.ProductMapper;
import co.com.bancolombia.model.commons.PageSummary;
import co.com.bancolombia.model.product.Product;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaginatedSearchResponseBuilder {

    private static final String BLANK = "";
    private static final String FORMAT_LINK_PAGE = "?pageNumber=%1$s&pageSize=%2$s";

    public static Mono<ServerResponse> paginationResponse(PageSummary<Product> productPageSummary) {

        return ServerResponse
                .status(getStatus(productPageSummary))
                .bodyValue(buildBodyResponse(productPageSummary));
    }

    private static HttpStatus getStatus(PageSummary<Product> productPageSummary) {
        if (!productPageSummary.getData().isEmpty()) {
            return HttpStatus.OK;
        }
        return HttpStatus.NO_CONTENT;
    }

    private static PaginationDTO<Object> buildBodyResponse(PageSummary<Product> productPageSummary) {

        List<ProductSearchResponseDTO> productList = new ArrayList<>();

        for (Product product : productPageSummary.getData()) {
            productList.add(ProductMapper.mapToProductSearchResponse(product));
        }
        return PaginationDTO.builder()
                .data(productList)
                .meta(buildMeta(productPageSummary.getTotalPages()))
                .links(buildLinksResponse(productPageSummary))
                .build();
    }

    private static Meta buildMeta(Integer totalPages) {
        return Meta.builder().totalPage(totalPages).build();
    }

    private static Links buildLinksResponse(PageSummary<Product> productPageSummary) {

        var pageNumber = productPageSummary.getPageNumber();
        var pageSize = productPageSummary.getPageRequest().getSize();
        var totalPages = productPageSummary.getTotalPages();

        return Links.builder()
                .self(getSelfOrLast(pageNumber, pageSize))
                .first(getTextLink(1, pageSize))
                .last(getSelfOrLast(totalPages, pageSize))
                .next(getNext(pageNumber, pageSize, productPageSummary.hasNext()))
                .prev(getPrev(pageNumber, pageSize, productPageSummary.hasPrevious()))
                .build();
    }

    private static String getNext(Integer pageNumber, Integer pageSize, boolean hasNext) {
        return hasNext ? getTextLink((pageNumber + 1), pageSize) : BLANK;
    }

    private static String getPrev(Integer pageNumber, Integer pageSize, boolean hasPrevious) {
        return hasPrevious ? getTextLink((pageNumber - 1), pageSize) : BLANK;
    }

    private static String getSelfOrLast(Integer pageNumber, Integer pageSize) {
        return getTextLink(pageNumber < 1 ? 1 : pageNumber, pageSize);
    }

    private static String getTextLink(Integer pageNumber, Integer pageSize) {
        return String.format(FORMAT_LINK_PAGE, pageNumber, pageSize);
    }

}