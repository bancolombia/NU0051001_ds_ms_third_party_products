package co.com.bancolombia.usecase.product;

import co.com.bancolombia.builders.ContextCreator;
import co.com.bancolombia.builders.ProductCreator;
import co.com.bancolombia.model.commons.Context;
import co.com.bancolombia.model.commons.Function;
import co.com.bancolombia.model.exception.BusinessException;
import co.com.bancolombia.model.exception.message.BusinessErrorMessage;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.enums.ProductState;
import co.com.bancolombia.model.product.gateway.ProductTypeAdapter;
import co.com.bancolombia.usecase.function.FunctionsProductUseCase;
import co.com.bancolombia.usecase.product.validations.ProductAccountValidationUseCase;
import co.com.bancolombia.usecase.product.validations.ProductValidationUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductUseCaseTest {

    private FunctionsProductUseCase functionsProductUseCase;

    private ProductValidationUseCase productValidationUseCase;

    private ProductAccountValidationUseCase productAccountValidationUseCase;

    private ProductTypeAdapter productTypeAdapter;

    private ProductUseCase productUseCase;


    private final String TEST_VARIABLE = "TEST";
    private Context context = ContextCreator.buildNewContext();

    @BeforeEach
    void setUp() {


        functionsProductUseCase = mock(FunctionsProductUseCase.class);
        productValidationUseCase = mock(ProductValidationUseCase.class);
        productAccountValidationUseCase = mock(ProductAccountValidationUseCase.class);
        productTypeAdapter = mock(ProductTypeAdapter.class);
        productUseCase = new ProductUseCase(functionsProductUseCase, productValidationUseCase,
                productAccountValidationUseCase);
    }

    @Test
    void shouldThrowNullPointerExceptionWhenParametersAreNullCreatingUseCase() {
        assertThrows(NullPointerException.class, () -> new ProductUseCase(null, productValidationUseCase,
                productAccountValidationUseCase));
        assertThrows(NullPointerException.class, () -> new ProductUseCase(functionsProductUseCase, null,
                productAccountValidationUseCase));
        assertThrows(NullPointerException.class, () -> new ProductUseCase(functionsProductUseCase, productValidationUseCase,
                null));

    }

    @Test
    void shouldValidateProductSuccessfully() {
        Product product = ProductCreator.buildNewProduct();
        when(productValidationUseCase.isProductValidToEnroll(product)).thenReturn(Mono.just(Boolean.TRUE));
        when(productAccountValidationUseCase.validateOwnership(product.getNumber(), product.getType(),
                product.getBeneficiary().getIdentification(), context)).thenReturn(Mono.just(Boolean.TRUE));
        StepVerifier.create(productUseCase.validateProduct(product, context))
                .expectNext(Boolean.TRUE)
                .verifyComplete();
    }


    @Test
    void shouldEnrollProductSuccessfully() {
        Product product = ProductCreator.buildNewProduct();
        Product expectedProduct = ProductCreator.buildNewProduct();
        expectedProduct.setState(ProductState.ACTIVE.getState());
        when(functionsProductUseCase.enrollProduct(product, context)).thenReturn(Mono.just(product.getFunctionList()));
        StepVerifier.create(productUseCase.enrollProduct(product, context))
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
        when(functionsProductUseCase.modifyProduct(product, expectedProduct, context))
                .thenReturn(Mono.just(expectedProduct.getFunctionList()));

        StepVerifier.create(productUseCase.modifyProduct(product, expectedProduct, context))
                .expectNextMatches(expectedProduct::equals)
                .verifyComplete();
    }


    @Test
    void shouldModifyProductFailedFunction() {
        Product product = ProductCreator.buildNewProduct();
        Product expectedProduct = ProductCreator.buildNewProduct();

        when(productValidationUseCase.isNewInformationProduct(product, expectedProduct
                .getName(), expectedProduct.getFunctionList())).thenReturn(Boolean.TRUE);
        when(functionsProductUseCase.modifyProduct(product, expectedProduct, context))
                .thenReturn(Mono.error(new BusinessException(BusinessErrorMessage.ERROR_IN_MODIFY_OF_PRODUCT_FUNCTION_MANAGER)));

        StepVerifier.create(productUseCase.modifyProduct(product, expectedProduct, context))
                .expectNext(product)
                .verifyComplete();
    }

    @Test
    void shouldThrowBusinessExceptionWhenFunctionsEnrolledAreEmpty() {
        Product product = ProductCreator.buildNewProduct();
        var exception = new BusinessException(BusinessErrorMessage.ERROR_IN_ENROLLMENT_OF_PRODUCT_FUNCTIONS);
        when(functionsProductUseCase.enrollProduct(product, context))
                .thenReturn(Mono.just(Set.of()));
        StepVerifier.create(productUseCase.enrollProduct(product, context))
                .expectErrorMatches(error -> error instanceof BusinessException
                        && error.getMessage().equals(exception.getMessage()))
                .verify();
    }

}
