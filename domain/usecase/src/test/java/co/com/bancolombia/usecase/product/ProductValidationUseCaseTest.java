package co.com.bancolombia.usecase.product;

import co.com.bancolombia.builders.BeneficiaryCreator;
import co.com.bancolombia.builders.CustomerCreator;
import co.com.bancolombia.builders.ProductCreator;
import co.com.bancolombia.model.commons.Function;
import co.com.bancolombia.model.entity.EntityAdapter;
import co.com.bancolombia.model.exception.BusinessException;
import co.com.bancolombia.model.exception.message.BusinessErrorMessage;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateway.ProductRepository;
import co.com.bancolombia.model.product.gateway.ProductTypeAdapter;
import co.com.bancolombia.model.user.Beneficiary;
import co.com.bancolombia.model.user.Customer;
import co.com.bancolombia.usecase.product.validations.ProductValidationUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertTrue;

class ProductValidationUseCaseTest {

    public static final String INTERNAL_CONTEXT_VALUE = "TIPDOC_FS000";
    public static final String INTERNAL_CONTEXT_VALUE_ONE = "TIPDOC_FS001";
    private EntityAdapter entityAdapter;
    private ProductTypeAdapter productTypeAdapter;
    private ProductRepository productRepository;
    private ProductValidationUseCase productValidationUseCase;

    private final String BANK_CODE = "0";
    private final String AFFILIATE_CODE = "1";
    private final HashSet<String> AFFILIATE_CODES = new HashSet<>();
    private final String TEST_VARIABLE = "TEST";
    private static final String CUSTOM_NAME = "Modified Product";

    @BeforeEach
    void setUp() {
        AFFILIATE_CODES.add(AFFILIATE_CODE);
        entityAdapter = mock(EntityAdapter.class);
        productTypeAdapter = mock(ProductTypeAdapter.class);
        productRepository = mock(ProductRepository.class);

        when(entityAdapter.getBankCode()).thenReturn(BANK_CODE);
        when(entityAdapter.getAffiliateCodes()).thenReturn(Mono.just(AFFILIATE_CODES));
        when(productTypeAdapter.getProductTypes()).thenReturn(Mono.just(Map.of(TEST_VARIABLE, TEST_VARIABLE)));
        when(productTypeAdapter.getIdentificationTypes()).thenReturn(Arrays.asList(INTERNAL_CONTEXT_VALUE, INTERNAL_CONTEXT_VALUE_ONE));

        productValidationUseCase = new ProductValidationUseCase(entityAdapter, productTypeAdapter, productRepository);
    }

    @Test
    void shouldThrowNullPointerExceptionWhenParametersAreNullCreatingUseCase() {
        assertThrows(NullPointerException.class, () -> new ProductValidationUseCase(null, productTypeAdapter, productRepository));
        assertThrows(NullPointerException.class, () -> new ProductValidationUseCase(entityAdapter, null, productRepository));
        assertThrows(NullPointerException.class, () -> new ProductValidationUseCase(entityAdapter, productTypeAdapter, null));
    }

    @Test
    void shouldThrowExceptionWhenBankCodeIsNull() {
        when(entityAdapter.getBankCode()).thenReturn(null);
        assertThrowsExactly(BusinessException.class, () -> new ProductValidationUseCase(entityAdapter, productTypeAdapter,
                        productRepository)
                , BusinessErrorMessage.INCORRECT_ENTITY_CODE.getMessage());
    }

    @ParameterizedTest
    @MethodSource("getIncorrectProductCreationVariables")
    void shouldThrowNullPointerExceptionWhenVariablesCreatingProductAreNull(String name, String number, String type,
                                                                            String entity, Set<Function> functions,
                                                                            Customer customer, Beneficiary beneficiary,
                                                                            BusinessErrorMessage businessErrorMessage) {
        Product product = Product.builder()
                .name(name)
                .number(number)
                .type(type)
                .entity(entity)
                .functionList(functions)
                .customer(customer)
                .beneficiary(beneficiary)
                .build();

        var exception = assertThrows(NullPointerException.class, () ->
                productValidationUseCase.isProductValidForCreation(product));
        assertEquals(businessErrorMessage.getMessage(), exception.getMessage());
    }

    @Test
    void shouldThrowBusinessExceptionWhenFunctionsAreEmpty() {
        Product product = ProductCreator.buildNewProduct();
        product.setFunctionList(Set.of());
        assertThrowsExactly(BusinessException.class, () -> productValidationUseCase.isProductValidForCreation(product)
                , BusinessErrorMessage.EMPTY_FUNCTIONS.getMessage());
    }

    @Test
    void shouldThrowBusinessExceptionWhenBeneficiaryAndCustomerIdentificationAreEquals() {
        Product product = ProductCreator.buildNewProduct();
        product.setBeneficiary(new Beneficiary(product.getCustomer().getIdentification(), ""));
        var exception = assertThrows(BusinessException.class, () ->
                productValidationUseCase.isProductValidForCreation(product));
        assertEquals(BusinessErrorMessage.BENEFICIARY_DIFFERENT_THAN_CUSTOMER, exception.getBusinessErrorMessage());
    }

    @Test
    void shouldReturnTrueWhenEntityBelongsToTheBank() {
        Product product = ProductCreator.buildNewProduct();
        product.setEntity(BANK_CODE);
        StepVerifier.create(productValidationUseCase.isProductEntityFromTheBank(product))
                .expectNext(Boolean.TRUE)
                .verifyComplete();
    }

    @Test
    void shouldReturnFalseWhenEntityBelongsToAffiliate() {
        Product product = ProductCreator.buildNewProduct();
        product.setEntity(AFFILIATE_CODE);
        StepVerifier.create(productValidationUseCase.isProductEntityFromTheBank(product))
                        .expectNext(Boolean.FALSE)
                                .verifyComplete();
    }

    @Test
    void shouldThrownBusinessExceptionWhenEntityCodeNotExist() {
        Product product = ProductCreator.buildNewProduct();

        StepVerifier.create(productValidationUseCase.isProductEntityFromTheBank(product))
                .expectErrorMatches(error -> error instanceof BusinessException && ((BusinessException) error)
                        .getBusinessErrorMessage().equals(BusinessErrorMessage.INCORRECT_ENTITY_CODE))
                .verify();
    }

    @Test
    void shouldThrowBusinessExceptionWhenIncorrectProductType() {
        Product product = ProductCreator.buildNewProduct();
        product.setType("NotExist");

        StepVerifier.create(productValidationUseCase.isProductTypeAuthorized(product))
                .expectErrorMatches(error -> error instanceof BusinessException && ((BusinessException) error)
                        .getBusinessErrorMessage().equals(BusinessErrorMessage.UNAVAILABLE_PRODUCT_TYPE))
                .verify();
    }

    @Test
    void shouldValidateProductSuccessfully() {
        Product product = ProductCreator.buildNewProduct();
        product.setType(TEST_VARIABLE);
        product.setFunctionList(Set.of(Function.values()));
        when(productRepository.isProductRegistered(product)).thenReturn(Mono.just(Boolean.TRUE));
        when(productRepository.isCustomNameAvailable(product.getCustomer(), product.getName()))
                .thenReturn(Mono.just(Boolean.TRUE));
        when(productRepository.isCustomerValid(product.getCustomer()))
                .thenReturn(Mono.just(Boolean.TRUE));

        StepVerifier.create(productValidationUseCase.isProductValidToEnroll(product))
                .expectNext(Boolean.TRUE)
                .verifyComplete();
    }

    @Test
    void shouldThrowBusinessExceptionWhenCustomerNameExist() {
        Product product = ProductCreator.buildNewProduct();
        product.setType(TEST_VARIABLE);
        when(productRepository.isCustomNameAvailable(product.getCustomer(), product.getName()))
                .thenReturn(Mono.just(Boolean.FALSE));
        when(productRepository.isCustomerValid(product.getCustomer()))
                .thenReturn(Mono.just(Boolean.TRUE));

        StepVerifier.create(productValidationUseCase.isProductValidToEnroll(product))
                .expectErrorMatches(error -> error instanceof BusinessException && ((BusinessException) error)
                        .getBusinessErrorMessage().equals(BusinessErrorMessage.CUSTOM_NAME_UNAVAILABLE))
                .verify();
    }

    @Test
    void shouldThrowBusinessExceptionWhenProductAlreadyExit() {
        Product product = ProductCreator.buildNewProduct();
        product.setType(TEST_VARIABLE);
        when(productRepository.isCustomNameAvailable(product.getCustomer(), product.getName()))
                .thenReturn(Mono.just(Boolean.TRUE));
        when(productRepository.isCustomerValid(product.getCustomer()))
                .thenReturn(Mono.just(Boolean.TRUE));
        when(productRepository.isProductRegistered(product)).thenReturn(Mono.just(Boolean.FALSE));

        StepVerifier.create(productValidationUseCase.isProductValidToEnroll(product))
                .expectErrorMatches(error -> error instanceof BusinessException && ((BusinessException) error)
                        .getBusinessErrorMessage().equals(BusinessErrorMessage.PRODUCT_EXIST))
                .verify();
    }

    @Test
    void shouldThrowBusinessExceptionWhenBeneficiaryTypeDoesNotExists() {
        Product product = ProductCreator.buildNewProductWithInvalidBeneficiaryDocumentType();
        product.setType(TEST_VARIABLE);
        when(productRepository.isCustomNameAvailable(product.getCustomer(), product.getName()))
                .thenReturn(Mono.just(Boolean.TRUE));
        when(productRepository.isProductRegistered(product)).thenReturn(Mono.just(Boolean.FALSE));

        assertThrowsExactly(BusinessException.class, () -> productValidationUseCase.isProductValidToEnroll(product)
                , BusinessErrorMessage.INVALID_IDENTIFICATION_TYPE.getMessage());
    }

    @Test
    void shouldIsCustomNameAvailableSuccessfully() {
        Customer customer = CustomerCreator.buildCustomer();
        when(productRepository.isCustomNameAvailable(customer, CUSTOM_NAME)).thenReturn(Mono.just(Boolean.TRUE));
        StepVerifier.create(productRepository.isCustomNameAvailable(customer, CUSTOM_NAME))
                .expectNext(Boolean.TRUE)
                .verifyComplete();
    }


    @Test
    void shouldIsNewInformationProductSuccessfully() {
        Product product = ProductCreator.buildNewProduct();
        Product productModify = ProductCreator.buildModifyBankProduct();
        boolean result = productValidationUseCase.isNewInformationProduct(product, CUSTOM_NAME, productModify.getFunctionList());
        assertTrue("true", result);
    }

    @Test
    void shouldIsNewInformationProductFailedForName() {
        Product product = ProductCreator.buildNewProduct();
        assertThrowsExactly(BusinessException.class, () -> productValidationUseCase
                        .isNewInformationProduct(product, product.getName(), product.getFunctionList())
                , BusinessErrorMessage.PRODUCT_INVALID_INFORMATION.getMessage());

    }

    @Test
    void shouldIsNewInformationProductFailedForFunctions() {
        Product product = ProductCreator.buildNewProduct();
        assertThrowsExactly(BusinessException.class, () -> productValidationUseCase
                        .isNewInformationProduct(product, CUSTOM_NAME, product.getFunctionList())
                , BusinessErrorMessage.PRODUCT_INVALID_INFORMATION.getMessage());

    }

    private static Stream<Arguments> getIncorrectProductCreationVariables() {
        return Stream.of(
                Arguments.of(null, "number", "type", "entity", Set.of(Function.values()),
                        CustomerCreator.buildCustomer(), BeneficiaryCreator.buildBeneficiary(),
                        BusinessErrorMessage.REQUIRED_NAME),
                Arguments.of("name", null, "type", "entity", Set.of(Function.values()),
                        CustomerCreator.buildCustomer(), BeneficiaryCreator.buildBeneficiary(),
                        BusinessErrorMessage.REQUIRED_PRODUCT_NUMBER),
                Arguments.of("name", "number", null, "entity", Set.of(Function.values()),
                        CustomerCreator.buildCustomer(), BeneficiaryCreator.buildBeneficiary(),
                        BusinessErrorMessage.REQUIRED_PRODUCT_TYPE),
                Arguments.of("name", "number", "type", null, Set.of(Function.values()),
                        CustomerCreator.buildCustomer(), BeneficiaryCreator.buildBeneficiary(),
                        BusinessErrorMessage.REQUIRED_ENTITY),
                Arguments.of("name", "number", "type", "entity", null,
                        CustomerCreator.buildCustomer(), BeneficiaryCreator.buildBeneficiary(),
                        BusinessErrorMessage.REQUIRED_FUNCTIONS),
                Arguments.of("name", "number", "type", "entity", Set.of(Function.values()),
                        null, BeneficiaryCreator.buildBeneficiary(),
                        BusinessErrorMessage.REQUIRED_CUSTOMER),
                Arguments.of("name", "number", "type", "entity", Set.of(Function.values()),
                        CustomerCreator.buildCustomer(), null,
                        BusinessErrorMessage.REQUIRED_BENEFICIARY)
        );
    }


}

