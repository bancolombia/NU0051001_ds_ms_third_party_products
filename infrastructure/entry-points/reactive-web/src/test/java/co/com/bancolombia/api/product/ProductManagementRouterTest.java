package co.com.bancolombia.api.product;

import co.com.bancolombia.api.builders.ProductTestBuilder;
import co.com.bancolombia.api.builders.RequestBuilder;
import co.com.bancolombia.api.commons.ContextBuilder;
import co.com.bancolombia.api.commons.validation.ConstraintValidator;
import co.com.bancolombia.api.exception.ExceptionHandler;
import co.com.bancolombia.api.product.dto.request.ProductEnrollmentRequestDTO;
import co.com.bancolombia.api.product.dto.request.ProductModifyRequestDTO;
import co.com.bancolombia.api.product.dto.response.ProductEnrollmentResponseDTO;
import co.com.bancolombia.api.product.dto.response.ProductModifyResponseDTO;
import co.com.bancolombia.api.properties.PageDefaultProperties;
import co.com.bancolombia.api.properties.RouteProperties;
import co.com.bancolombia.builders.ContextCreator;
import co.com.bancolombia.builders.ProductCreator;
import co.com.bancolombia.model.commons.Context;
import co.com.bancolombia.model.commons.Function;
import co.com.bancolombia.model.commons.PageRequest;
import co.com.bancolombia.model.commons.PageSummary;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.usecase.product.ProductManagementUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@WebFluxTest(properties = {
        "routes.path-mapping.product.enroll=/product",
        "routes.path-mapping.product.modify=/product/{productId}",
        "routes.path-mapping.product.delete=/product/{productId}",
        "routes.path-mapping.product.paginatedSearch=/products/active",
        "routes.path-mapping.product.download=/products/active/download",
        "settings.pagination.default-page-size=10",
        "settings.pagination.default-page-number=1"
})
@ContextConfiguration(classes = {
        RouteProperties.class,
        ProductManagementRouter.class,
        ConstraintValidator.class,
        ContextBuilder.class,
        PageDefaultProperties.class,
        ProductManagementHandler.class,
        ExceptionHandler.class,
})
class ProductManagementRouterTest {

    public static final String CUSTOM_NAME = "CustomName";
    private static final String PRODUCT_ID = "TESTID";
    private static final String MODIFY_PRODUCT_URI = "/product/".concat(PRODUCT_ID);
    private static final String PAGINATED_SEARCH_PRODUCT = "/products/active";
    private static final String GENERATE_PRODUCT_REPORT = "/products/active/download";
    private static final String PAGE_NUMBER_PARAM = "pageNumber";
    private static final Integer PAGE_NUMBER_PARAM_VALUE = 2;
    private static final String PAGE_SIZE_PARAM = "pageSize";
    private static final Integer PAGE_SIZE_PARAM_VALUE = 1;
    private static final Long TOTAL_REGISTERS = 100L;
    public static final String EMPTY_CUSTOM_NAME = "";
    private final String PRODUCT_ID_QUERY_PARAM = "productId";
    private static final String DELETE_PRODUCT_URI = "/product/".concat(PRODUCT_ID);

    private Context context = ContextCreator.buildNewContext();

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private ProductManagementUseCase productManagementUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldGetEnrolledProductSuccessfully() {
        String enrollProductURI = "/product";

        var enrolledProduct = ProductCreator.buildEnrolledBankProduct();
        ProductEnrollmentRequestDTO productEnrollmentRequestDTO = ProductTestBuilder.buildProductEnrollmentRequestDTO();

        when(productManagementUseCase.enrollProduct(any(Product.class), eq(context)))
                .thenReturn(Mono.just(enrolledProduct));

        var response = ProductTestBuilder
                .buildProductEnrollmentResponseDTO(enrolledProduct.getState());

        webClient.post()
                .uri(enrollProductURI)
                .headers(RequestBuilder.getContextHeaders)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(productEnrollmentRequestDTO)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CREATED)
                .expectBody(ProductEnrollmentResponseDTO.class)
                .isEqualTo(response);
    }

    @Test
    void shouldModifyProductSuccessfully() {
        var modifyProduct = ProductCreator.buildModifyBankProduct();
        ProductModifyRequestDTO productModifyRequestDTO = ProductTestBuilder.buildProductModifyRequestDTO();

        when(productManagementUseCase.modifyProduct(PRODUCT_ID, ProductTestBuilder.CUSTOM_NAME,
                Set.of(Function.findFunction(ProductTestBuilder.CODE)), context)).thenReturn(Mono.just(modifyProduct));

        var response = ProductTestBuilder
                .buildProductModifyResponseDTO(modifyProduct.getName(), modifyProduct.getFunctionList());

        webClient.patch()
                .uri(uriBuilder -> uriBuilder
                        .path(MODIFY_PRODUCT_URI)
                        .build())
                .headers(RequestBuilder.getContextHeaders)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(productModifyRequestDTO)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK)
                .expectBody(ProductModifyResponseDTO.class)
                .isEqualTo(response);
    }

    @Test
    void shouldModifyProductFailWhenNoCustomerName() {
        ProductModifyRequestDTO productModifyRequestDTO = new ProductModifyRequestDTO(null, null);

        webClient.patch()
                .uri(uriBuilder -> uriBuilder
                        .path(MODIFY_PRODUCT_URI)
                        .build())
                .headers(RequestBuilder.getContextHeaders)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(productModifyRequestDTO)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldModifyProductFailWhenEmptyCustomerName() {
        ProductModifyRequestDTO productModifyRequestDTO = new ProductModifyRequestDTO(EMPTY_CUSTOM_NAME, null);

        webClient.patch()
                .uri(uriBuilder -> uriBuilder
                        .path(MODIFY_PRODUCT_URI)
                        .build())
                .headers(RequestBuilder.getContextHeaders)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(productModifyRequestDTO)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldModifyProductFailWhenEmptyFunctionList() {
        ProductModifyRequestDTO productModifyRequestDTO = new ProductModifyRequestDTO(CUSTOM_NAME, new HashSet<>());

        webClient.patch()
                .uri(uriBuilder -> uriBuilder
                        .path(MODIFY_PRODUCT_URI)
                        .build())
                .headers(RequestBuilder.getContextHeaders)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(productModifyRequestDTO)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldDeleteProductSuccessfully() {
        when(productManagementUseCase.deleteProduct(context, PRODUCT_ID)).thenReturn(Mono.just(Boolean.TRUE));
        webClient.delete()
                .uri(uriBuilder -> uriBuilder
                        .path(DELETE_PRODUCT_URI)
                        .build())
                .headers(RequestBuilder.getContextHeaders)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NO_CONTENT);

    }

    @Test
    void shouldSearchPaginatedProductsSuccessfully() {
        var searchProduct = ProductCreator.buildSearchProduct();
        var pagesummary = new PageSummary<Product>(List.of(searchProduct, searchProduct),
                new PageRequest(PAGE_NUMBER_PARAM_VALUE, PAGE_SIZE_PARAM_VALUE),
                TOTAL_REGISTERS);

        var productSearchRequestDTO = ProductTestBuilder.buildProductSearchRequestDTO();

        when(productManagementUseCase.searchPaginatedProducts(any(), any(), any(), any()))
                .thenReturn(Mono.just(pagesummary));

        webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(PAGINATED_SEARCH_PRODUCT)
                        .queryParam(PAGE_NUMBER_PARAM, PAGE_NUMBER_PARAM_VALUE)
                        .queryParam(PAGE_SIZE_PARAM, PAGE_SIZE_PARAM_VALUE)
                        .build())
                .bodyValue(productSearchRequestDTO)
                .headers(RequestBuilder.getContextHeaders)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK)
                .expectBody()
                .jsonPath("$.meta").isNotEmpty()
                .jsonPath("$.meta.totalPage").isEqualTo(TOTAL_REGISTERS)
                .jsonPath("$.data").isNotEmpty()
                .jsonPath("$.data[0].customName").isEqualTo("TEST")
                .jsonPath("$.data[0].beneficiary.identification.number").isEqualTo("12345678")
                .jsonPath("$.data[1].customName").isEqualTo("TEST")
                .jsonPath("$.data[1].beneficiary.identification.number").isEqualTo("12345678")
                .jsonPath("$.links").isNotEmpty()
                .jsonPath("$.links.self").isEqualTo("?pageNumber=2&pageSize=1")
                .jsonPath("$.links.first").isEqualTo("?pageNumber=1&pageSize=1")
                .jsonPath("$.links.prev").isEqualTo("?pageNumber=1&pageSize=1")
                .jsonPath("$.links.next").isEqualTo("?pageNumber=3&pageSize=1")
                .jsonPath("$.links.last").isEqualTo("?pageNumber=100&pageSize=1");
    }

    @Test
    void shouldGenerateReportSuccessfully() {
        var productSearchRequestDTO = ProductTestBuilder.buildProductSearchRequestDTO();

       when(productManagementUseCase.generateReport(any(), any(), any(), any())).thenReturn(Mono.empty());

        webClient.post()
                .uri(GENERATE_PRODUCT_REPORT)
                .bodyValue(productSearchRequestDTO)
                .headers(RequestBuilder.getDownloadHeaders)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.ACCEPTED);
    }

}

