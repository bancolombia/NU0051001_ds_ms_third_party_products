package co.com.bancolombia.usecase.function;

import co.com.bancolombia.builders.ContextCreator;
import co.com.bancolombia.builders.ProductCreator;
import co.com.bancolombia.model.commons.Context;
import co.com.bancolombia.model.commons.Function;
import co.com.bancolombia.model.entity.EntityAdapter;
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


class FunctionsAffiliateProductUseCaseTest {

    private FunctionsAffiliateProductUseCase functionsAffiliateProductUseCase;
    private FunctionAdapter functionAdapter;
    private EntityAdapter entityAdapter;

    private Context context = ContextCreator.buildNewContext();

    private Product product = ProductCreator.buildNewProduct();

    private String NEQUI_BANK_CODE = "1507";

    @BeforeEach
    void setUp() {
        functionAdapter = mock(FunctionAdapter.class);
        entityAdapter = mock(EntityAdapter.class);
        functionsAffiliateProductUseCase = new FunctionsAffiliateProductUseCase(functionAdapter, entityAdapter);
        setUpEnrollmentMockedBehavior(true, true, true, true);
        setUpDeleteMockedBehavior(true, true, true, true);
        when(entityAdapter.getNequiBankCode()).thenReturn(NEQUI_BANK_CODE);
    }

    void setUpEnrollmentMockedBehavior(Boolean enrollProductForSupplierPayment, Boolean enrollProductForPayrollPayment,
                                       Boolean enrollProductForTransferOtherBanks, Boolean enrollProductForTransferNequiAccount) {
        when(functionAdapter.enrollProductForSupplierPayment(any(Product.class), any(Context.class)))
                .thenReturn(Mono.just(enrollProductForSupplierPayment));
        when(functionAdapter.enrollProductForPayrollPayment(any(Product.class), any(Context.class)))
                .thenReturn(Mono.just(enrollProductForPayrollPayment));
        when(functionAdapter.enrollProductForTransferOtherBanks(any(Product.class), any(Context.class)))
                .thenReturn(Mono.just(enrollProductForTransferOtherBanks));
        when(functionAdapter.enrollProductForTransferNequiAccount(any(Product.class), any(Context.class)))
                .thenReturn(Mono.just(enrollProductForTransferNequiAccount));
    }

    void setUpDeleteMockedBehavior(Boolean deleteProductForPayrollPayment, Boolean deleteProductForSupplierPayment,
                                   Boolean deleteProductForTransferNequiAccount,
                                   Boolean deleteProductForTransferOtherBanks) {
        when(functionAdapter.deleteProductForPayrollPayment(any(Product.class), any(Context.class)))
                .thenReturn(Mono.just(deleteProductForPayrollPayment));
        when(functionAdapter.deleteProductForSupplierPayment(any(Product.class), any(Context.class)))
                .thenReturn(Mono.just(deleteProductForSupplierPayment));
        when(functionAdapter.deleteProductForTransferNequiAccount(any(Product.class), any(Context.class)))
                .thenReturn(Mono.just(deleteProductForTransferNequiAccount));
        when(functionAdapter.deleteProductForTransferOtherBanks(any(Product.class), any(Context.class)))
                .thenReturn(Mono.just(deleteProductForTransferOtherBanks));
    }

    @Test
    void shouldEnrollProductWithAllFunctionsActiveSuccessfully() {

        product.setFunctionList(Set.of(Function.values()));
        StepVerifier.create(functionsAffiliateProductUseCase.enrollProduct(product, context))
                .expectNext(Set.of(Function.values()))
                .verifyComplete();

        product.setFunctionList(Set.of(Function.PAYROLL_PAYMENT, Function.SUPPLIER_PAYMENT));
        StepVerifier.create(functionsAffiliateProductUseCase.enrollProduct(product, context))
                .expectNext(Set.of(Function.PAYROLL_PAYMENT, Function.SUPPLIER_PAYMENT))
                .verifyComplete();

        product.setFunctionList(Set.of(Function.SUPPLIER_PAYMENT));
        StepVerifier.create(functionsAffiliateProductUseCase.enrollProduct(product, context))
                .expectNext(Set.of(Function.SUPPLIER_PAYMENT))
                .verifyComplete();
    }

    @Test
    void shouldNotEnrollProductWithAllFunctionsInactive() {
        setUpEnrollmentMockedBehavior(false, false,
                false, false);

        product.setFunctionList(Set.of(Function.values()));
        StepVerifier.create(functionsAffiliateProductUseCase.enrollProduct(product, context))
                .expectNext(Set.of())
                .verifyComplete();

        product.setFunctionList(Set.of(Function.PAYROLL_PAYMENT, Function.SUPPLIER_PAYMENT));
        StepVerifier.create(functionsAffiliateProductUseCase.enrollProduct(product, context))
                .expectNext(Set.of())
                .verifyComplete();

        product.setFunctionList(Set.of(Function.SUPPLIER_PAYMENT));
        StepVerifier.create(functionsAffiliateProductUseCase.enrollProduct(product, context))
                .expectNext(Set.of())
                .verifyComplete();
    }

    @Test
    void shouldNotEnrollProductForSupplierPaymentWhenAdapterReturnFalse() {
        setUpEnrollmentMockedBehavior(false, true,
                true, true);
        product.setFunctionList(Set.of(Function.values()));
        StepVerifier.create(functionsAffiliateProductUseCase.enrollProduct(product, context))
                .expectNext(Set.of(Function.TRANSFER, Function.PAYROLL_PAYMENT))
                .verifyComplete();
    }

    @Test
    void shouldNotEnrollProductForPayrollPaymentWhenAdapterReturnFalse() {
        setUpEnrollmentMockedBehavior(true, false,
                true, true);
        product.setFunctionList(Set.of(Function.values()));
        StepVerifier.create(functionsAffiliateProductUseCase.enrollProduct(product, context))
                .expectNext(Set.of(Function.TRANSFER, Function.SUPPLIER_PAYMENT))
                .verifyComplete();
    }

    @Test
    void shouldNotEnrollProductForTransferWhenAdapterReturnFalse() {
        setUpEnrollmentMockedBehavior(true, true,
                false, false);
        product.setFunctionList(Set.of(Function.values()));
        StepVerifier.create(functionsAffiliateProductUseCase.enrollProduct(product, context))
                .expectNext(Set.of(Function.SUPPLIER_PAYMENT, Function.PAYROLL_PAYMENT))
                .verifyComplete();
    }

    @Test
    void shouldNotEnrollProductForNequiTransferWhenAdapterReturnFalse() {
        setUpEnrollmentMockedBehavior(true, true,
                true, false);
        product.setEntity(NEQUI_BANK_CODE);
        product.setFunctionList(Set.of(Function.values()));
        StepVerifier.create(functionsAffiliateProductUseCase.enrollProduct(product, context))
                .expectNext(Set.of(Function.SUPPLIER_PAYMENT, Function.PAYROLL_PAYMENT))
                .verifyComplete();
    }

    @Test
    void shouldEnrollProductForNequiTransferWhenAdapterReturnTrue() {
        setUpEnrollmentMockedBehavior(true, true,
                false, true);
        product.setEntity(NEQUI_BANK_CODE);
        product.setFunctionList(Set.of(Function.TRANSFER, Function.SUPPLIER_PAYMENT));
        StepVerifier.create(functionsAffiliateProductUseCase.enrollProduct(product, context))
                .expectNext(Set.of(Function.TRANSFER, Function.SUPPLIER_PAYMENT))
                .verifyComplete();
    }

    @Test
    void shouldNotEnrollAnyFunctionWithEmptySetOfFunctionsSuccessfully() {
        product.setFunctionList(Set.of());
        StepVerifier.create(functionsAffiliateProductUseCase.enrollProduct(product, context))
                .expectNext(Set.of())
                .verifyComplete();
    }

    @Test
    void shouldThrowErrorIfSetOfFunctionsIsNull() {
        product.setFunctionList(null);
        assertThrows(NullPointerException.class, () -> functionsAffiliateProductUseCase.enrollProduct(product, context));
    }

    @Test
    void shouldDeletePayrollAndTransferFunctionSuccessfullyWhenModifyProduct() {
        Product newProduct = ProductCreator.buildNewProduct();
        product.setFunctionList(Set.of(Function.values()));
        newProduct.setFunctionList(Set.of(Function.SUPPLIER_PAYMENT));
        StepVerifier.create(functionsAffiliateProductUseCase.modifyProduct(product, newProduct, context))
                .expectNext(Set.of(Function.SUPPLIER_PAYMENT))
                .verifyComplete();
    }

    @Test
    void shouldDeleteSupplierAndTransferFunctionSuccessfullyWhenModifyProduct() {
        Product newProduct = ProductCreator.buildNewProduct();
        product.setFunctionList(Set.of(Function.values()));
        newProduct.setFunctionList(Set.of(Function.PAYROLL_PAYMENT));
        StepVerifier.create(functionsAffiliateProductUseCase.modifyProduct(product, newProduct, context))
                .expectNext(Set.of(Function.PAYROLL_PAYMENT))
                .verifyComplete();
    }

    @Test
    void shouldDeleteSupplierAndPayrollFunctionSuccessfullyWhenModifyProduct() {
        Product newProduct = ProductCreator.buildNewProduct();
        product.setFunctionList(Set.of(Function.values()));
        newProduct.setFunctionList(Set.of(Function.TRANSFER));
        StepVerifier.create(functionsAffiliateProductUseCase.modifyProduct(product, newProduct, context))
                .expectNext(Set.of(Function.TRANSFER))
                .verifyComplete();
    }

    @Test
    void shouldDeleteAllFunctionsSuccessfullyWhenModifyProduct() {
        Product newProduct = ProductCreator.buildNewProduct();
        product.setFunctionList(Set.of(Function.values()));
        newProduct.setFunctionList(Set.of());
        StepVerifier.create(functionsAffiliateProductUseCase.modifyProduct(product, newProduct, context))
                .expectNext(Set.of())
                .verifyComplete();
    }

    @Test
    void shouldEnrollAllFunctionsSuccessfullyWhenModifyProduct() {
        Product newProduct = ProductCreator.buildNewProduct();
        product.setFunctionList(Set.of());
        newProduct.setFunctionList(Set.of(Function.values()));
        StepVerifier.create(functionsAffiliateProductUseCase.modifyProduct(product, newProduct, context))
                .expectNext(Set.of(Function.TRANSFER, Function.SUPPLIER_PAYMENT, Function.PAYROLL_PAYMENT))
                .verifyComplete();
    }

    @Test
    void shouldDeleteAllFunctionsSuccessfullyWhenDeleteProduct() {
        Product newProduct = ProductCreator.buildNewProduct();
        product.setFunctionList(Set.of(Function.values()));
        StepVerifier.create(functionsAffiliateProductUseCase.deleteProduct(product, context))
                .expectNext(Set.of(Function.TRANSFER, Function.SUPPLIER_PAYMENT, Function.PAYROLL_PAYMENT))
                .verifyComplete();
    }

    @Test
    void shouldNotDeleteAllFunctionsSuccessfullyWhenDeleteProductAndAdapterResponseIsFalse() {
        setUpDeleteMockedBehavior(false, false, false, false);
        Product newProduct = ProductCreator.buildNewProduct();
        product.setFunctionList(Set.of(Function.values()));
        StepVerifier.create(functionsAffiliateProductUseCase.deleteProduct(product, context))
                .expectNext(Set.of())
                .verifyComplete();
    }
}
