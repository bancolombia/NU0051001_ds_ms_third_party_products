package co.com.bancolombia.api.product;

import co.com.bancolombia.api.commons.ContextBuilder;
import co.com.bancolombia.api.commons.RequestHelper;
import co.com.bancolombia.api.commons.validation.ConstraintValidator;
import co.com.bancolombia.api.product.builder.PaginatedSearchResponseBuilder;
import co.com.bancolombia.api.product.dto.request.ProductEnrollmentRequestDTO;
import co.com.bancolombia.api.product.dto.request.ProductModifyRequestDTO;
import co.com.bancolombia.api.product.dto.request.ProductSearchRequestDTO;
import co.com.bancolombia.api.product.mapper.ProductMapper;
import co.com.bancolombia.api.product.mapper.SearchCriteriaMapper;
import co.com.bancolombia.api.properties.PageDefaultProperties;
import co.com.bancolombia.model.product.report.AvailableFormat;
import co.com.bancolombia.usecase.product.ProductManagementUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.function.TupleUtils;

import static co.com.bancolombia.api.commons.RequestHelper.getPageRequest;
import static org.springframework.http.HttpStatus.CREATED;

@Component
@RequiredArgsConstructor
public class ProductManagementHandler {

    private final ProductManagementUseCase productManagementUseCase;
    private final ContextBuilder contextBuilder;
    private final PageDefaultProperties defaultPage;
    private final ConstraintValidator constraintValidator;
    private static final String PRODUCT_ID = "productId";

    public Mono<ServerResponse> enrollProduct(ServerRequest serverRequest) {

        return Mono.zip(serverRequest.bodyToMono(ProductEnrollmentRequestDTO.class)
                                .flatMap(constraintValidator::validateData),
                        contextBuilder.getContext(serverRequest))
                .flatMap(enrollmentTuple -> productManagementUseCase.enrollProduct(
                        ProductMapper.mapToProduct(enrollmentTuple.getT1(), enrollmentTuple.getT2())
                        , enrollmentTuple.getT2()))
                .map(ProductMapper::mapToProductEnrollmentResponse)
                .flatMap(productEnrollmentResponseDTO -> ServerResponse.status(CREATED)
                        .bodyValue(productEnrollmentResponseDTO));
    }

    public Mono<ServerResponse> searchPaginatedProducts(ServerRequest serverRequest) {
        return Mono.zip(serverRequest.bodyToMono(ProductSearchRequestDTO.class),
                        contextBuilder.getContext(serverRequest))
                .flatMap(TupleUtils.function((productSearchRequestDTO, context) -> Mono.zip(
                        Mono.just(SearchCriteriaMapper.mapSearchCriteriaToProduct(productSearchRequestDTO, context)),
                        Mono.just(getPageRequest(serverRequest, defaultPage)),
                        Mono.just(context),
                        Mono.just(SearchCriteriaMapper.mapSearchCriteriaToDateRange(productSearchRequestDTO))
                )))
                .flatMap(TupleUtils.function(productManagementUseCase::searchPaginatedProducts))
                .flatMap(PaginatedSearchResponseBuilder::paginationResponse);
    }


    public Mono<ServerResponse> downloadActiveProducts(ServerRequest serverRequest) {
        var format = RequestHelper.getAvailableFormatHeader(serverRequest);

        return Mono.zip(serverRequest.bodyToMono(ProductSearchRequestDTO.class),
                        contextBuilder.getContext(serverRequest))
                .flatMap(searchTuple -> productManagementUseCase.generateReport(
                        SearchCriteriaMapper.mapSearchCriteriaToProduct(
                                searchTuple.getT1(), searchTuple.getT2()),
                        searchTuple.getT2(),
                        SearchCriteriaMapper.mapSearchCriteriaToDateRange(searchTuple.getT1()),
                        format
                ))
                .then(ServerResponse.status(HttpStatus.ACCEPTED).build());
    }

    public Mono<ServerResponse> modifyProduct(ServerRequest serverRequest) {
        var productId = serverRequest.pathVariable(PRODUCT_ID);
        return contextBuilder.getContext(serverRequest)
                .flatMap(context -> serverRequest.bodyToMono(ProductModifyRequestDTO.class)
                        .map(ProductMapper::mapToData)
                        .flatMap(result -> productManagementUseCase.modifyProduct(productId, result.getCustomName(),
                                        result.getFunctions(), context)
                                .map(product -> ProductMapper.mapToProductModifyResponse(product,
                                        result.getCustomName(), result.getFunctions()))))
                .flatMap(productModifyResponseDTO -> ServerResponse.status(HttpStatus.OK)
                        .bodyValue(productModifyResponseDTO));
    }

    public Mono<ServerResponse> deleteProduct(ServerRequest serverRequest) {
        var productId = serverRequest.pathVariable(PRODUCT_ID);
        return contextBuilder.getContext(serverRequest)
                .flatMap(result -> productManagementUseCase.deleteProduct(result, productId))
                .flatMap(response -> ServerResponse.status(HttpStatus.NO_CONTENT).build());
    }
}
