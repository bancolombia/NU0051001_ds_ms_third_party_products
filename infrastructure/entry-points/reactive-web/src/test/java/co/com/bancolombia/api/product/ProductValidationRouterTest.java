package co.com.bancolombia.api.product;

import co.com.bancolombia.api.builders.ErrorResponseBuilder;
import co.com.bancolombia.api.builders.ProductValidationTestBuilder;
import co.com.bancolombia.api.builders.RequestBuilder;
import co.com.bancolombia.api.commons.ContextBuilder;
import co.com.bancolombia.api.commons.enums.RequiredQueryParam;
import co.com.bancolombia.api.commons.validation.ConstraintValidator;
import co.com.bancolombia.api.exception.ExceptionHandler;
import co.com.bancolombia.api.exception.dto.ErrorResponse;
import co.com.bancolombia.api.properties.RouteProperties;
import co.com.bancolombia.api.validations.ProductValidationHandler;
import co.com.bancolombia.api.validations.ProductValidationRouter;
import co.com.bancolombia.api.validations.dto.response.OwnershipValidationResponseDTO;
import co.com.bancolombia.builders.BeneficiaryCreator;
import co.com.bancolombia.builders.ContextCreator;
import co.com.bancolombia.builders.IdentificationCreator;
import co.com.bancolombia.model.commons.Context;
import co.com.bancolombia.model.exception.message.BusinessErrorMessage;
import co.com.bancolombia.model.user.Identification;
import co.com.bancolombia.model.user.gateway.CustomerAdapter;
import co.com.bancolombia.usecase.product.validations.ProductAccountValidationUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@WebFluxTest(properties = {
        "routes.path-mapping.product.validateOwnership=/product-ownership-validation/{productNumber}",
})
@ContextConfiguration(classes = {
        RouteProperties.class,
        ProductValidationRouter.class,
        ConstraintValidator.class,
        ContextBuilder.class,
        ProductValidationHandler.class,
        ExceptionHandler.class,
})
class ProductValidationRouterTest {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private ProductAccountValidationUseCase productAccountValidationUseCase;
    @MockBean
    private CustomerAdapter customerAdapter;

    private final String PRODUCT_NUMBER = "1";
    private final String PRODUCT_TYPE = "test";
    private final String VALIDATE_OWNERSHIP_URI = "/product-ownership-validation/".concat(PRODUCT_NUMBER);
    private final String PRODUCT_TYPE_QUERY_PARAM = "product[type]";
    private final String IDENTIFICATION_TYPE_QUERY_PARAM = "identification[type]";
    private final String IDENTIFICATION_NUMBER_QUERY_PARAM = "identification[number]";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldValidateOwnershipSuccessfully() {

        Identification identification = IdentificationCreator.buildIdentification();
        Context context = ContextCreator.buildNewContext();
        var response = ProductValidationTestBuilder.buildOwnershipValidationResponseDTO();

        when(productAccountValidationUseCase
                .validateOwnership(PRODUCT_NUMBER, PRODUCT_TYPE, identification, context))
                .thenReturn(Mono.just(true));
        when(customerAdapter.getCustomerName(identification, context)).thenReturn(Mono.just(BeneficiaryCreator.NAME));

        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(VALIDATE_OWNERSHIP_URI)
                        .queryParam(PRODUCT_TYPE_QUERY_PARAM, PRODUCT_TYPE)
                        .queryParam(IDENTIFICATION_TYPE_QUERY_PARAM, identification.type())
                        .queryParam(IDENTIFICATION_NUMBER_QUERY_PARAM, identification.number())
                        .build())
                .headers(RequestBuilder.getContextHeaders)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK)
                .expectBody(OwnershipValidationResponseDTO.class)
                .isEqualTo(response);
    }

    @ParameterizedTest
    @CsvSource({
            "PRODUCT_TYPE, REQUIRED_PRODUCT_TYPE",
            "IDENTIFICATION_TYPE, REQUIRED_IDENTIFICATION_TYPE",
            "IDENTIFICATION_NUMBER, REQUIRED_IDENTIFICATION_NUMBER",
    })
    void shouldThrowBusinessExceptionWhenMissingQueryParam(String queryParamToRemove, String businessError) {
        Identification identification = IdentificationCreator.buildIdentification();
        BusinessErrorMessage businessErrorMessage = BusinessErrorMessage.valueOf(businessError);
        var errorResponse = ErrorResponseBuilder.buildErrorResponse(businessErrorMessage.getMessage(),
                VALIDATE_OWNERSHIP_URI, businessErrorMessage.getCode(), businessErrorMessage.getMessage());

        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(VALIDATE_OWNERSHIP_URI)
                        .queryParam(PRODUCT_TYPE_QUERY_PARAM, PRODUCT_TYPE)
                        .queryParam(IDENTIFICATION_TYPE_QUERY_PARAM, identification.type())
                        .queryParam(IDENTIFICATION_NUMBER_QUERY_PARAM, identification.number())
                        .replaceQueryParam(RequiredQueryParam.valueOf(queryParamToRemove).getValue())
                        .build())
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST)
                .expectBody(ErrorResponse.class)
                .isEqualTo(errorResponse);
    }
}