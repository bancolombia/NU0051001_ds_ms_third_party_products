package co.com.bancolombia.usecase.function;

import co.com.bancolombia.builders.ContextCreator;
import co.com.bancolombia.builders.ProductCreator;
import co.com.bancolombia.model.commons.Context;
import co.com.bancolombia.model.commons.Function;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateway.function.FunctionAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FunctionsProductUseCaseTest {


    private FunctionsProductUseCase functionsProductUseCase;
    private FunctionAdapter functionAdapter;

    private Context context = ContextCreator.buildNewContext();

    private Product product = ProductCreator.buildNewProduct();


    @BeforeEach
    void setUp() {
        functionAdapter = mock(FunctionAdapter.class);
        functionsProductUseCase = new FunctionsProductUseCase(functionAdapter);
        setUpEnrollmentMockedBehavior(true, true);
        setUpDeleteMockedBehavior(true, true);
    }

    void setUpEnrollmentMockedBehavior(Boolean enrollProductForSupplierPayment, Boolean enrollProductForPayrollPayment) {
        when(functionAdapter.enrollProductForSupplierPayment(any(Product.class), any(Context.class)))
                .thenReturn(Mono.just(enrollProductForSupplierPayment));
        when(functionAdapter.enrollProductForPayrollPayment(any(Product.class), any(Context.class)))
                .thenReturn(Mono.just(enrollProductForPayrollPayment));
    }

    void setUpDeleteMockedBehavior(Boolean deleteProductForPayrollPayment, Boolean deleteProductForSupplierPayment) {
        when(functionAdapter.deleteProductForPayrollPayment(any(Product.class), any(Context.class)))
                .thenReturn(Mono.just(deleteProductForPayrollPayment));
        when(functionAdapter.deleteProductForSupplierPayment(any(Product.class), any(Context.class)))
                .thenReturn(Mono.just(deleteProductForSupplierPayment));
    }

    @Test
    void shouldEnrollProductWithAllFunctionsActiveSuccessfully() {

        product.setFunctionList(Set.of(Function.values()));
        StepVerifier.create(functionsProductUseCase.enrollProduct(product, context))
                .expectNext(Set.of(Function.values()))
                .verifyComplete();

        product.setFunctionList(Set.of(Function.PAYROLL_PAYMENT, Function.SUPPLIER_PAYMENT));
        StepVerifier.create(functionsProductUseCase.enrollProduct(product, context))
                .expectNext(Set.of(Function.PAYROLL_PAYMENT, Function.SUPPLIER_PAYMENT))
                .verifyComplete();

        product.setFunctionList(Set.of(Function.SUPPLIER_PAYMENT));
        StepVerifier.create(functionsProductUseCase.enrollProduct(product, context))
                .expectNext(Set.of(Function.SUPPLIER_PAYMENT))
                .verifyComplete();
    }

    @Test
    void shouldNotEnrollProductWithAllFunctionsInactive() {
        setUpEnrollmentMockedBehavior(false, false);

        product.setFunctionList(Set.of(Function.values()));
        StepVerifier.create(functionsProductUseCase.enrollProduct(product, context))
                .expectNext(Set.of(Function.TRANSFER))
                .verifyComplete();

        product.setFunctionList(Set.of(Function.PAYROLL_PAYMENT, Function.SUPPLIER_PAYMENT));
        StepVerifier.create(functionsProductUseCase.enrollProduct(product, context))
                .expectNext(Set.of())
                .verifyComplete();

        product.setFunctionList(Set.of(Function.SUPPLIER_PAYMENT));
        StepVerifier.create(functionsProductUseCase.enrollProduct(product, context))
                .expectNext(Set.of())
                .verifyComplete();
    }

    @Test
    void shouldNotEnrollProductForSupplierPaymentWhenAdapterReturnFalse() {
        setUpEnrollmentMockedBehavior(false, true);
        product.setFunctionList(Set.of(Function.values()));
        StepVerifier.create(functionsProductUseCase.enrollProduct(product, context))
                .expectNext(Set.of(Function.TRANSFER, Function.PAYROLL_PAYMENT))
                .verifyComplete();
    }

    @Test
    void shouldNotEnrollProductForPayrollPaymentWhenAdapterReturnFalse() {
        setUpEnrollmentMockedBehavior(true, false);
        product.setFunctionList(Set.of(Function.values()));
        StepVerifier.create(functionsProductUseCase.enrollProduct(product, context))
                .expectNext(Set.of(Function.TRANSFER, Function.SUPPLIER_PAYMENT))
                .verifyComplete();
    }

    @Test
    void shouldReturnTransferFunctionWhenEnroll() {
        product.setFunctionList(Set.of(Function.TRANSFER));
        StepVerifier.create(functionsProductUseCase.enrollProduct(product, context))
                .expectNext(Set.of(Function.TRANSFER))
                .verifyComplete();
    }

    @Test
    void shouldNotEnrollAnyFunctionWithEmptySetOfFunctionsSuccessfully() {
        product.setFunctionList(Set.of());
        StepVerifier.create(functionsProductUseCase.enrollProduct(product, context))
                .expectNext(Set.of())
                .verifyComplete();
    }

    @Test
    void shouldThrowErrorIfSetOfFunctionsIsNull() {
        product.setFunctionList(null);
        assertThrows(NullPointerException.class, () -> functionsProductUseCase.enrollProduct(product, context));
    }

    @Test
    void shouldDeletePayrollAndTransferFunctionSuccessfullyWhenModifyProduct() {
        Product newProduct = ProductCreator.buildNewProduct();
        product.setFunctionList(Set.of(Function.values()));
        newProduct.setFunctionList(Set.of(Function.SUPPLIER_PAYMENT));
        StepVerifier.create(functionsProductUseCase.modifyProduct(product, newProduct, context))
                .expectNext(Set.of(Function.SUPPLIER_PAYMENT))
                .verifyComplete();
    }

    @Test
    void shouldDeleteSupplierAndTransferFunctionSuccessfullyWhenModifyProduct() {
        Product newProduct = ProductCreator.buildNewProduct();
        product.setFunctionList(Set.of(Function.values()));
        newProduct.setFunctionList(Set.of(Function.PAYROLL_PAYMENT));
        StepVerifier.create(functionsProductUseCase.modifyProduct(product, newProduct, context))
                .expectNext(Set.of(Function.PAYROLL_PAYMENT))
                .verifyComplete();
    }

    @Test
    void shouldDeleteSupplierAndPayrollFunctionSuccessfullyWhenModifyProduct() {
        Product newProduct = ProductCreator.buildNewProduct();
        product.setFunctionList(Set.of(Function.values()));
        newProduct.setFunctionList(Set.of(Function.TRANSFER));
        StepVerifier.create(functionsProductUseCase.modifyProduct(product, newProduct, context))
                .expectNext(Set.of(Function.TRANSFER))
                .verifyComplete();
    }

    @Test
    void shouldDeleteAllFunctionsSuccessfullyWhenModifyProduct() {
        Product newProduct = ProductCreator.buildNewProduct();
        product.setFunctionList(Set.of(Function.values()));
        newProduct.setFunctionList(Set.of());
        StepVerifier.create(functionsProductUseCase.modifyProduct(product, newProduct, context))
                .expectNext(Set.of())
                .verifyComplete();
    }

    @Test
    void shouldEnrollAllFunctionsSuccessfullyWhenModifyProduct() {
        Product newProduct = ProductCreator.buildNewProduct();
        product.setFunctionList(Set.of());
        newProduct.setFunctionList(Set.of(Function.values()));
        StepVerifier.create(functionsProductUseCase.modifyProduct(product, newProduct, context))
                .expectNext(Set.of(Function.values()))
                .verifyComplete();
    }

    @Test
    void shouldDeleteAllFunctionsSuccessfullyWhenDeleteProduct() {
        product.setFunctionList(Set.of(Function.values()));
        StepVerifier.create(functionsProductUseCase.deleteProduct(product, context))
                .expectNext(Set.of(Function.values()))
                .verifyComplete();
    }

    @Test
    void shouldDeleteOnlyTransfersFunctionWhenDeleteProductAdapterResponseIsFalse() {
        setUpDeleteMockedBehavior(false, false);
        product.setFunctionList(Set.of(Function.values()));
        StepVerifier.create(functionsProductUseCase.deleteProduct(product, context))
                .expectNext(Set.of(Function.TRANSFER))
                .verifyComplete();
    }

}
