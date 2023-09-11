package co.com.bancolombia.usecase.product;

import co.com.bancolombia.builders.ContextCreator;
import co.com.bancolombia.builders.ProductCreator;
import co.com.bancolombia.model.commons.Context;
import co.com.bancolombia.model.exception.BusinessException;
import co.com.bancolombia.model.exception.message.BusinessErrorMessage;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateway.ProductAccountValidationAdapter;
import co.com.bancolombia.model.user.Identification;
import co.com.bancolombia.usecase.product.validations.ProductAccountValidationUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class ProductAccountValidationUseCaseTest {

    @Mock
    private ProductAccountValidationAdapter productAccountValidationAdapter;

    @InjectMocks
    private ProductAccountValidationUseCase productAccountValidationUseCase;

    private static Context context = ContextCreator.buildNewContext();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldValidateNotNullParameters() {
        Product product = ProductCreator.buildNewProduct();
        String productNumber = product.getNumber();
        String productType = product.getType();
        Identification identification = product.getBeneficiary().getIdentification();

        assertThrows(NullPointerException.class, () -> productAccountValidationUseCase
                .validateOwnership(null, productType, identification, context));
        assertThrows(NullPointerException.class, () -> productAccountValidationUseCase
                .validateOwnership(productNumber, null, identification, context));
        assertThrows(NullPointerException.class, () -> productAccountValidationUseCase
                .validateOwnership(productNumber, productType, null, context));
        assertThrows(NullPointerException.class, () -> productAccountValidationUseCase
                .validateOwnership(productNumber, productType, identification, null));
    }

    @Test
    void shouldValidateOwnerShipSuccessfully() {
        Product product = ProductCreator.buildNewProduct();
        when(productAccountValidationAdapter.validateOwnership(product.getNumber(), product.getType(),
                product.getBeneficiary().getIdentification(), context))
                .thenReturn(Mono.just(Boolean.TRUE));
        StepVerifier.create(productAccountValidationUseCase.validateOwnership(product.getNumber(), product.getType(),
                        product.getBeneficiary().getIdentification(), context))
                .expectNext(Boolean.TRUE)
                .verifyComplete();
    }

    @Test
    void shouldThrowBusinessExceptionWhenCustomerDoesNotBelongInBank() {
        Product product = ProductCreator.buildNewProduct();
        var exception = new BusinessException(BusinessErrorMessage.INVALID_OWNERSHIP);
        when(productAccountValidationAdapter.validateOwnership(product.getNumber(), product.getType(),
                product.getBeneficiary().getIdentification(), context))
                .thenReturn(Mono.just(Boolean.FALSE));
        StepVerifier.create(productAccountValidationUseCase.validateOwnership(product.getNumber(), product.getType(),
                        product.getBeneficiary().getIdentification(), context))
                .expectErrorMatches(error -> error instanceof BusinessException
                        && error.getMessage().equals(exception.getMessage()))
                .verify();
    }
}
