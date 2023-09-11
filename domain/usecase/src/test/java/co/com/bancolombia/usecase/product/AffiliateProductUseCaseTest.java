package co.com.bancolombia.usecase.product;

import co.com.bancolombia.builders.ContextCreator;
import co.com.bancolombia.builders.ProductCreator;
import co.com.bancolombia.model.commons.Context;
import co.com.bancolombia.model.exception.BusinessException;
import co.com.bancolombia.model.exception.message.BusinessErrorMessage;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.enums.ProductState;
import co.com.bancolombia.model.product.gateway.ProductTypeAdapter;
import co.com.bancolombia.usecase.function.FunctionsAffiliateProductUseCase;
import co.com.bancolombia.usecase.product.validations.ProductValidationUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AffiliateProductUseCaseTest {

    private static final String EXTRA_PRODUCT = "TEST";

    private FunctionsAffiliateProductUseCase functionsAffiliateProductUseCase;

    private ProductValidationUseCase productValidationUseCase;

    private ProductTypeAdapter productTypeAdapter;

    private AffiliateProductUseCase affiliateProductUseCase;

    private Context context = ContextCreator.buildNewContext();

    @BeforeEach
    void setUp() {
        functionsAffiliateProductUseCase = mock(FunctionsAffiliateProductUseCase.class);
        productValidationUseCase = mock(ProductValidationUseCase.class);
        productTypeAdapter = mock(ProductTypeAdapter.class);
        affiliateProductUseCase = new AffiliateProductUseCase(functionsAffiliateProductUseCase, productValidationUseCase);

    }

    @Test
    void shouldThrowNullPointerExceptionWhenParametersAreNullCreatingUseCase() {
        assertThrows(NullPointerException.class, () -> new AffiliateProductUseCase(null
                , productValidationUseCase));
        assertThrows(NullPointerException.class, () -> new AffiliateProductUseCase(functionsAffiliateProductUseCase, null));
    }

    @Test
    void shouldValidateProductSuccessfully() {
        Product product = ProductCreator.buildNewProduct();
        when(productValidationUseCase.isProductValidToEnroll(product)).thenReturn(Mono.just(Boolean.TRUE));
        StepVerifier.create(affiliateProductUseCase.validateProduct(product, context))
                .expectNext(Boolean.TRUE)
                .verifyComplete();
    }


    @Test
    void shouldEnrollProductSuccessfully() {
        Product product = ProductCreator.buildNewProduct();
        Product expectedProduct = ProductCreator.buildNewProduct();
        expectedProduct.setState(ProductState.PENDING_APPROVAL.getState());
        when(functionsAffiliateProductUseCase.enrollProduct(product, context)).thenReturn(Mono.just(product.getFunctionList()));
        StepVerifier.create(affiliateProductUseCase.enrollProduct(product, context))
                .expectNext(expectedProduct)
                .verifyComplete();
    }

    @Test
    void shouldModifyProductSuccessfully() {
        Product product = ProductCreator.buildNewProduct();
        Product expectedProduct = ProductCreator.buildNewProduct();

        when(productValidationUseCase.isNewInformationProduct(product, expectedProduct
                .getName(), expectedProduct.getFunctionList()))
                .thenReturn(Boolean.TRUE);
        when(functionsAffiliateProductUseCase.modifyProduct(product, expectedProduct, context))
                .thenReturn(Mono.just(expectedProduct.getFunctionList()));

        StepVerifier.create(affiliateProductUseCase.modifyProduct(product, expectedProduct, context))
                .expectNextMatches(expectedProduct::equals)
                .verifyComplete();
    }

    @Test
    void shouldModifyProductFailedFunction() {
        Product product = ProductCreator.buildNewModifyProduct();
        Product expectedProduct = ProductCreator.buildNewProduct();
        when(productValidationUseCase.isNewInformationProduct(product, expectedProduct
                .getName(), expectedProduct.getFunctionList()))
                .thenReturn(Boolean.TRUE);
        when(functionsAffiliateProductUseCase.modifyProduct(product, expectedProduct, context))
                .thenReturn(Mono.error(new BusinessException(BusinessErrorMessage.ERROR_IN_MODIFY_OF_PRODUCT_FUNCTION_MANAGER)));

        StepVerifier.create(affiliateProductUseCase.modifyProduct(product, expectedProduct, context))
                .expectNext(product)
                .verifyComplete();
    }

    @Test
    void shouldThrowBusinessExceptionWhenFunctionsEnrolledAreEmpty() {
        Product product = ProductCreator.buildNewProduct();
        var exception = new BusinessException(BusinessErrorMessage.ERROR_IN_ENROLLMENT_OF_PRODUCT_FUNCTIONS);
        when(functionsAffiliateProductUseCase.enrollProduct(product, context))
                .thenReturn(Mono.just(Set.of()));
        StepVerifier.create(affiliateProductUseCase.enrollProduct(product, context))
                .expectErrorMatches(error -> error instanceof BusinessException
                        && error.getMessage().equals(exception.getMessage()))
                .verify();
    }


}
