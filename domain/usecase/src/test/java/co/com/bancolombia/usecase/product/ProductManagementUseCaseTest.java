package co.com.bancolombia.usecase.product;

import co.com.bancolombia.builders.ContextCreator;
import co.com.bancolombia.builders.ProductCreator;
import co.com.bancolombia.model.commons.Context;
import co.com.bancolombia.model.commons.DateRange;
import co.com.bancolombia.model.commons.Function;
import co.com.bancolombia.model.commons.PageRequest;
import co.com.bancolombia.model.commons.PageSummary;
import co.com.bancolombia.model.event.ProductManagementEventPublisher;
import co.com.bancolombia.model.exception.BusinessException;
import co.com.bancolombia.model.exception.message.BusinessErrorMessage;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.enums.BankProductAvailableFunctions;
import co.com.bancolombia.model.product.gateway.ProductManager;
import co.com.bancolombia.model.product.gateway.ProductRepository;
import co.com.bancolombia.model.product.report.AvailableFormat;
import co.com.bancolombia.model.product.report.ProductsReportAdapter;
import co.com.bancolombia.usecase.product.validations.ProductValidationUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductManagementUseCaseTest {

    public static final String FILE_NAME = "filename";
    @Mock
    private ProductUseCase productUseCase;

    @Mock
    private ProductManager productManager;

    @Mock
    private AffiliateProductUseCase affiliateProductUseCase;

    @Mock
    private ProductValidationUseCase productValidationUseCase;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductManagementEventPublisher eventPublisher;

    @Mock
    private ProductsReportAdapter productsReportAdapter;

    @InjectMocks
    private ProductManagementUseCase productManagementUseCase;

    @Mock
    private ProductManagementUseCase productManagement;

    private Map<String, String> productTypes;
    private Map<String, Set<Function>> authorizedFunctions;
    private static final String TEST_VARIABLE = "TEST";
    private static final String CUSTOM_NAME = "Modified Product";
    private static final String PRODUCT_ID = "sampleProductId";
    private static final String TYPE = "SAVING_ACCOUNT";
    private final Context context = ContextCreator.buildNewContext();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productTypes = Map.of(BankProductAvailableFunctions.SAVING_ACCOUNT.name(), BankProductAvailableFunctions.SAVING_ACCOUNT.name(),
                BankProductAvailableFunctions.CURRENT_ACCOUNT.name(), BankProductAvailableFunctions.CURRENT_ACCOUNT.name(),
                BankProductAvailableFunctions.ELECTRONIC_ACCOUNT.name(), BankProductAvailableFunctions.ELECTRONIC_ACCOUNT.name(),
                TEST_VARIABLE, TEST_VARIABLE);
        authorizedFunctions = Arrays.stream(BankProductAvailableFunctions.values())
                .collect(Collectors.toUnmodifiableMap(bankProductTypeAuthorizedFunctions ->
                                productTypes.get(bankProductTypeAuthorizedFunctions.name()),
                        BankProductAvailableFunctions::getFunctions));
    }

    @Test
    void shouldThrowNullPointerExceptionWhenObjectsAreNullInEnrollProduct() {
        Product product = ProductCreator.buildNewProduct();
        assertThrows(NullPointerException.class, () -> productManagementUseCase.enrollProduct(null, context));
        assertThrows(NullPointerException.class, () -> productManagementUseCase.enrollProduct(product, null));
    }

    @Test
    void shouldThrowNullPointerExceptionWhenObjectsAreNullInModifyProduct() {
        assertThrows(NullPointerException.class, () -> productManagementUseCase.modifyProduct(null, null, null, context));
        assertThrows(NullPointerException.class, () -> productManagementUseCase.modifyProduct("id", null, null, null));
    }

    @Test
    void shouldThrowNullPointerExceptionWhenObjectsAreNullInDeleteProduct() {
        assertThrows(NullPointerException.class, () -> productManagementUseCase.deleteProduct(null, "id"));
        assertThrows(NullPointerException.class, () -> productManagementUseCase.deleteProduct(context, null));
    }

    @Test
    void shouldEnrollBankProductSuccessfully() {
        Product product = ProductCreator.buildNewProduct();
        Product expectedProduct = ProductCreator.buildEnrolledBankProduct();

        when(productValidationUseCase.isProductEntityFromTheBank(product)).thenReturn(Mono.just(Boolean.TRUE));
        when(productValidationUseCase.isProductValidToEnroll(product)).thenReturn(Mono.just(Boolean.TRUE));
        when(productUseCase.validateProduct(product, context)).thenReturn(Mono.just(Boolean.TRUE));
        when(productUseCase.enrollProduct(product, context)).thenAnswer(res -> {
            Product enrolledProduct = res.getArgument(0);
            enrolledProduct.setState(ProductCreator.ACTIVE_STATE);
            return Mono.just(enrolledProduct);
        });

        when(productRepository.enrollProduct(any(Product.class))).thenAnswer(res -> {
            Product enrolledProduct = res.getArgument(0);
            enrolledProduct.setId(ProductCreator.ID);
            return Mono.just(enrolledProduct);
        });


        StepVerifier.create(productManagementUseCase.enrollProduct(product, context))
                .expectNext(expectedProduct)
                .verifyComplete();

        verify(eventPublisher).emitSuccessfulProductEnrollmentEvent(expectedProduct, context);
    }

    @Test
    void shouldEnrollAffiliateProductSuccessfully() {
        Product product = ProductCreator.buildNewProduct();
        Product expectedProduct = ProductCreator.buildEnrolledBankProduct();

        when(productValidationUseCase.isProductEntityFromTheBank(product)).thenReturn(Mono.just(Boolean.FALSE));
        when(affiliateProductUseCase.validateProduct(product, context)).thenReturn(Mono.just(Boolean.TRUE));
        when(affiliateProductUseCase.enrollProduct(product, context)).thenAnswer(res -> {
            Product enrolledProduct = res.getArgument(0);
            enrolledProduct.setState(ProductCreator.ACTIVE_STATE);
            return Mono.just(enrolledProduct);
        });

        when(productRepository.enrollProduct(any(Product.class))).thenAnswer(res -> {
            Product enrolledProduct = res.getArgument(0);
            enrolledProduct.setId(ProductCreator.ID);
            return Mono.just(enrolledProduct);
        });


        StepVerifier.create(productManagementUseCase.enrollProduct(product, context))
                .expectNext(expectedProduct)
                .verifyComplete();

        verify(eventPublisher).emitSuccessfulProductEnrollmentEvent(expectedProduct, context);
    }

    @Test
    void shouldEmitFailedProductEnrollmentEventWhenUnexpectedError() {
        Product product = ProductCreator.buildNewProduct();

        Throwable throwable = new RuntimeException();
        when(productValidationUseCase.isProductEntityFromTheBank(product)).thenReturn(Mono.just(Boolean.FALSE));
        when(affiliateProductUseCase.validateProduct(product, context)).thenReturn(Mono.error(throwable));
        when(affiliateProductUseCase.enrollProduct(product, context)).thenReturn(Mono.error(throwable));

        StepVerifier.create(productManagementUseCase.enrollProduct(product, context))
                .expectError(throwable.getClass())
                .verify();

        verify(eventPublisher).emitFailedProductEnrollmentEvent(product, context, throwable);
    }

    @Test
    void shouldModifyProductSuccessful() {

        Product product = ProductCreator.buildNewModifyProduct();
        product.setId(PRODUCT_ID);
        product.setType(TYPE);

        Product newProduct = ProductCreator.buildNewProduct();
        newProduct.setName(CUSTOM_NAME);
        newProduct.setFunctionList(Set.of(Function.PAYROLL_PAYMENT));

        var productManagementUseCaseSpy = spy(productManagementUseCase);

        when(productRepository.getProduct(context.getCustomer(), PRODUCT_ID)).thenReturn(Mono.just(product));
        when(productValidationUseCase.isProductTypeAuthorized(product)).thenReturn(Mono.just(Boolean.TRUE));
        when(productRepository.isCustomNameAvailable(context.getCustomer(), CUSTOM_NAME)).thenReturn(Mono.just(Boolean.TRUE));
        when(productValidationUseCase.isNewInformationProduct(product, CUSTOM_NAME, newProduct.getFunctionList())).thenReturn(Boolean.TRUE);
        when(productValidationUseCase.isProductEntityFromTheBank(product)).thenReturn(Mono.just(Boolean.TRUE));
        when(productManagementUseCaseSpy.createNewProduct(product, CUSTOM_NAME, newProduct.getFunctionList())).thenReturn(newProduct);
        when(productUseCase.modifyProduct(product, newProduct, context)).thenReturn(Mono.just(newProduct));
        when(productRepository.modifyProduct(context.getCustomer(), newProduct)).thenReturn(Mono.just(newProduct));

        StepVerifier.create(productManagementUseCaseSpy.modifyProduct(PRODUCT_ID, CUSTOM_NAME, newProduct.getFunctionList(), context))
                .expectNext(newProduct)
                .verifyComplete();
        verify(eventPublisher).emitSuccessfulProductModifyEvent(newProduct, context);

    }

    @Test
    void shouldModifyProductFailed() {
        Product product = ProductCreator.buildNewModifyProduct();
        product.setId(PRODUCT_ID);
        product.setType(TYPE);

        Product newProduct = ProductCreator.buildNewProduct();
        newProduct.setName(CUSTOM_NAME);
        newProduct.setFunctionList(Set.of(Function.PAYROLL_PAYMENT));

        var productManagementUseCaseSpy = spy(productManagementUseCase);

        Throwable throwable = new RuntimeException();

        when(productRepository.getProduct(context.getCustomer(), PRODUCT_ID)).thenReturn(Mono.just(product));
        when(productValidationUseCase.isProductTypeAuthorized(product)).thenReturn(Mono.just(Boolean.TRUE));
        when(productRepository.isCustomNameAvailable(context.getCustomer(), CUSTOM_NAME)).thenReturn(Mono.error(throwable));
        when(productValidationUseCase.isNewInformationProduct(product, CUSTOM_NAME, newProduct.getFunctionList())).thenReturn(Boolean.TRUE);
        when(productValidationUseCase.isProductEntityFromTheBank(product)).thenReturn(Mono.just(Boolean.TRUE));
        when(productManagementUseCaseSpy.createNewProduct(product, CUSTOM_NAME, newProduct.getFunctionList())).thenReturn(newProduct);
        when(productUseCase.modifyProduct(product, newProduct, context)).thenReturn(Mono.just(newProduct));
        when(productRepository.modifyProduct(context.getCustomer(), newProduct)).thenReturn(Mono.just(newProduct));

        StepVerifier.create(productManagementUseCaseSpy.modifyProduct(PRODUCT_ID, CUSTOM_NAME, newProduct.getFunctionList(), context))
                .expectError(throwable.getClass())
                .verify();
        verify(eventPublisher).emitFailedProductModifyEvent(product, context, throwable);
    }

    @Test
    void shouldModifyProductFailedProductNotExist() {

        Product product = ProductCreator.buildNewModifyProduct();
        product.setId(PRODUCT_ID);
        product.setType(TYPE);

        Throwable error = new BusinessException(BusinessErrorMessage.PRODUCT_NOT_EXIST);
        when(productRepository.getProduct(context.getCustomer(), PRODUCT_ID))
                .thenReturn(Mono.error(error));

        StepVerifier.create(productManagementUseCase.modifyProduct(PRODUCT_ID, CUSTOM_NAME, product.getFunctionList(), context))
                .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
                        ((BusinessException) throwable).getBusinessErrorMessage() == BusinessErrorMessage.PRODUCT_NOT_EXIST)
                .verify();

        verify(eventPublisher).emitFailedProductModifyEvent(Product.builder().id(PRODUCT_ID).build(), context, error);
    }

    @Test
    void shouldCreateProductReturnsProductCopy() {

        Product product = ProductCreator.buildNewProduct();
        Product productModify = ProductCreator.buildModifyBankProduct();

        var result = productManagementUseCase.createNewProduct(product, CUSTOM_NAME, productModify.getFunctionList());

        assertEquals(CUSTOM_NAME, result.getName());
        assertEquals(productModify.getFunctionList(), result.getFunctionList());
    }

    @Test
    void shouldCreateProductWhenCustomNameIsEmpty() {

        Product product = ProductCreator.buildNewProduct();
        Product productModify = ProductCreator.buildModifyBankProduct();

        var result = productManagementUseCase.createNewProduct(product, null, productModify.getFunctionList());

        assertEquals(product.getName(), result.getName());
        assertEquals(productModify.getFunctionList(), result.getFunctionList());
    }

    @Test
    void shouldCreateProductWhenFunctionsIsEmpty() {

        Product product = ProductCreator.buildNewProduct();

        var result = productManagementUseCase.createNewProduct(product, CUSTOM_NAME, null);

        assertEquals(CUSTOM_NAME, result.getName());
        assertEquals(product.getFunctionList(), result.getFunctionList());
    }

    @Test
    void shouldSearchPaginatedProductsSuccessfully() {
        Product searchProduct = ProductCreator.buildSearchProduct();
        PageRequest pageRequest = new PageRequest(1, 1);
        DateRange dateRange = new DateRange(LocalDateTime.now(), LocalDateTime.now().plusDays(1));

        Product product = ProductCreator.buildNewProduct();
        List<Product> data = new ArrayList<>();
        data.add(product);
        PageSummary<Product> pageSummary = new PageSummary<>(data, pageRequest, 1L);

        when(productRepository.searchPaginatedProducts(searchProduct, pageRequest, context, dateRange))
                .thenReturn(Mono.just(pageSummary));

        StepVerifier.create(productManagementUseCase.searchPaginatedProducts(searchProduct, pageRequest, context, dateRange))
                .expectNext(pageSummary)
                .verifyComplete();

        verify(eventPublisher).emitSuccessfulSearchPaginatedProductsEvent(context, searchProduct, dateRange);
    }

    @Test
    void shouldThrowNotFoundErrorWhenSearchingNonExistingProducts() {
        Product searchProduct = ProductCreator.buildSearchProduct();
        PageRequest pageRequest = new PageRequest(1, 1);
        DateRange dateRange = new DateRange(LocalDateTime.now(), LocalDateTime.now().plusDays(1));

        List<Product> data = new ArrayList<>();
        PageSummary<Product> pageSummary = new PageSummary<>(data, pageRequest, 1L);

        when(productRepository.searchPaginatedProducts(searchProduct, pageRequest, context, dateRange))
                .thenReturn(Mono.just(pageSummary));

        StepVerifier.create(productManagementUseCase.searchPaginatedProducts(searchProduct, pageRequest, context, dateRange))
                .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
                        ((BusinessException) throwable).getBusinessErrorMessage() == BusinessErrorMessage.NON_PRODUCTS_FOUND)
                .verify();

        verify(eventPublisher).emitFailedSearchPaginatedProductsEvent(any(), any(), any(), any());
    }

    @Test
    void shouldEmitFailedPaginatedProductsSearchEventWhenAnErrorOccurred() {
        Product searchProduct = ProductCreator.buildSearchProduct();
        PageRequest pageRequest = new PageRequest(1, 1);
        DateRange dateRange = new DateRange(LocalDateTime.now(), LocalDateTime.now().plusDays(1));

        Throwable throwable = new RuntimeException();

        when(productRepository.searchPaginatedProducts(searchProduct, pageRequest, context, dateRange))
                .thenReturn(Mono.error(throwable));

        StepVerifier.create(productManagementUseCase.searchPaginatedProducts(searchProduct, pageRequest, context, dateRange))
                .expectError()
                .verify();
        verify(eventPublisher).emitFailedSearchPaginatedProductsEvent(ArgumentMatchers.eq(context),
                ArgumentMatchers.eq(searchProduct),
                ArgumentMatchers.eq(dateRange), any(Throwable.class));
    }

    @Test
    void shouldThrowNullPointerExceptionWhenParametersAreNullPaginatedProductsSearch() {
        Product searchProduct = ProductCreator.buildSearchProduct();
        PageRequest pageRequest = new PageRequest(1, 1);
        DateRange dateRange = new DateRange(null, null);

        assertThrows(NullPointerException.class, () -> productManagementUseCase.searchPaginatedProducts(searchProduct, pageRequest, context, null));
        assertThrows(NullPointerException.class, () -> productManagementUseCase.searchPaginatedProducts(searchProduct, pageRequest, null, dateRange));
        assertThrows(NullPointerException.class, () -> productManagementUseCase.searchPaginatedProducts(searchProduct, null, context, dateRange));
        assertThrows(NullPointerException.class, () -> productManagementUseCase.searchPaginatedProducts(searchProduct, null, null, dateRange));
        assertThrows(NullPointerException.class, () -> productManagementUseCase.searchPaginatedProducts(null, pageRequest, context, dateRange));
        assertThrows(NullPointerException.class, () -> productManagementUseCase.searchPaginatedProducts(null, pageRequest, null, dateRange));
        assertThrows(NullPointerException.class, () -> productManagementUseCase.searchPaginatedProducts(null, null, context, dateRange));
        assertThrows(NullPointerException.class, () -> productManagementUseCase.searchPaginatedProducts(null, null, null, dateRange));
        assertThrows(NullPointerException.class, () -> productManagementUseCase.searchPaginatedProducts(searchProduct, pageRequest, null, null));
        assertThrows(NullPointerException.class, () -> productManagementUseCase.searchPaginatedProducts(searchProduct, null, context, null));
        assertThrows(NullPointerException.class, () -> productManagementUseCase.searchPaginatedProducts(searchProduct, null, null, null));
        assertThrows(NullPointerException.class, () -> productManagementUseCase.searchPaginatedProducts(null, pageRequest, context, null));
        assertThrows(NullPointerException.class, () -> productManagementUseCase.searchPaginatedProducts(null, pageRequest, null, null));
        assertThrows(NullPointerException.class, () -> productManagementUseCase.searchPaginatedProducts(null, null, context, null));
        assertThrows(NullPointerException.class, () -> productManagementUseCase.searchPaginatedProducts(null, null, null, null));
    }

    @Test
    void shouldDeleteProductSuccessfully() {

        when(productRepository.getProduct(context.getCustomer(), PRODUCT_ID))
                .thenReturn(Mono.just(Product.builder().build()));

        when(productRepository.deleteProduct(context.getCustomer(), PRODUCT_ID))
                .thenReturn(Mono.just(Boolean.TRUE));

        StepVerifier.create(productManagementUseCase.deleteProduct(context, PRODUCT_ID))
                .expectNext(Boolean.TRUE)
                .verifyComplete();

        verify(eventPublisher).emitSuccessfulDeleteProductEvent(Product.builder().build(), context);
    }

    @Test
    void shouldDeleteProductFailed() {

        Throwable throwable = new RuntimeException();

        when(productRepository.getProduct(context.getCustomer(), PRODUCT_ID))
                .thenReturn(Mono.just(Product.builder().build()));

        when(productRepository.deleteProduct(context.getCustomer(), PRODUCT_ID))
                .thenReturn(Mono.error(throwable));

        StepVerifier.create(productManagementUseCase.deleteProduct(context, PRODUCT_ID))
                .expectError(throwable.getClass())
                .verify();

        verify(eventPublisher).emitFailedDeleteProductEvent(Product.builder().build(), context, throwable);
    }

    @Test
    void shouldDeleteProductFailedProductNotExist() {

        Throwable throwable = new BusinessException(BusinessErrorMessage.PRODUCT_NOT_EXIST);
        when(productRepository.getProduct(context.getCustomer(), PRODUCT_ID))
                .thenReturn(Mono.error(throwable));

        StepVerifier.create(productManagementUseCase.deleteProduct(context, PRODUCT_ID))
                .expectError(throwable.getClass())
                .verify();
        verify(eventPublisher).emitFailedDeleteProductEvent(Product.builder().id(PRODUCT_ID).build(), context, throwable);
    }

    @Test
    void shouldGenerateReportWhenProductExists() {
        Product searchProduct = ProductCreator.buildSearchProduct();
        PageRequest pageRequest = new PageRequest(1, 1);
        DateRange dateRange = new DateRange(LocalDateTime.now(), LocalDateTime.now().plusDays(1));

        List<Product> data = new ArrayList<>();
        data.add(searchProduct);
        PageSummary<Product> pageSummary = new PageSummary<>(data, pageRequest, 1L);

        Mono<byte[]> report = Mono.just(new byte[10]);

        when(productRepository.searchPaginatedProducts(any(Product.class),
                any(PageRequest.class),
                any(Context.class),
                any(DateRange.class)))
                .thenReturn(Mono.just(pageSummary));

        when(productsReportAdapter.generateReport(data, AvailableFormat.CSV)).thenReturn(report);
        when(productsReportAdapter.uploadReport(any(), any(), any())).thenReturn(Mono.just(FILE_NAME));
        when(eventPublisher.
                emitSuccessfulProductEnrolledDownloadEvent(context, searchProduct, dateRange,
                        FILE_NAME, AvailableFormat.CSV)).thenReturn(Mono.empty());

        StepVerifier.create(productManagementUseCase.generateReport(searchProduct, context, dateRange,
                        AvailableFormat.CSV))
                .verifyComplete();
    }

    @Test
    void shouldGenerateReportWhenMultipleProductsExists() {
        Product searchProduct = ProductCreator.buildSearchProduct();
        PageRequest pageRequest = new PageRequest(1, 1);
        DateRange dateRange = new DateRange(LocalDateTime.now(), LocalDateTime.now().plusDays(1));

        List<Product> data = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            data.add(searchProduct);
        }
        PageSummary<Product> pageSummary = new PageSummary<>(data, pageRequest, Long.valueOf(data.size()));

        Mono<byte[]> report = Mono.just(new byte[10]);

        when(productRepository.searchPaginatedProducts(any(Product.class),
                any(PageRequest.class),
                any(Context.class),
                any(DateRange.class)))
                .thenReturn(Mono.just(pageSummary));

        when(productsReportAdapter.generateReport(data, AvailableFormat.CSV)).thenReturn(report);
        when(productsReportAdapter.uploadReport(any(), any(), any())).thenReturn(Mono.just(FILE_NAME));
        when(eventPublisher.
                emitSuccessfulProductEnrolledDownloadEvent(context, searchProduct, dateRange,
                        FILE_NAME, AvailableFormat.CSV)).thenReturn(Mono.empty());

        StepVerifier.create(productManagementUseCase.generateReport(searchProduct, context, dateRange,
                        AvailableFormat.CSV))
                .verifyComplete();
    }

    @Test
    void shouldNotGenerateReportWhenProductNotExists() {
        Product searchProduct = ProductCreator.buildSearchProduct();
        PageRequest pageRequest = new PageRequest(1);
        DateRange dateRange = new DateRange(LocalDateTime.now(), LocalDateTime.now().plusDays(1));

        List<Product> data = new ArrayList<>();
        PageSummary<Product> pageSummary = new PageSummary<>(data, pageRequest, 1L);

        when(eventPublisher.emitFailedProductEnrolledDownloadEvent(any(), any(),
                any(), any(), any())).thenReturn(Mono.empty());

        when(productRepository.searchPaginatedProducts(any(Product.class),
                any(PageRequest.class),
                any(Context.class),
                any(DateRange.class)))
                .thenReturn(Mono.just(pageSummary));

        StepVerifier.create(productManagementUseCase.generateReport(searchProduct, context, dateRange,
                        AvailableFormat.CSV))
                .verifyErrorMatches(throwable -> BusinessErrorMessage.NON_PRODUCTS_FOUND
                        .equals(((BusinessException) throwable).getBusinessErrorMessage()));
    }


    @Test
    void shouldThrowNullPointerExceptionWhenParametersAreNullGenerateReport() {
        Product searchProduct = ProductCreator.buildSearchProduct();
        DateRange dateRange = new DateRange(null, null);

        assertThrows(NullPointerException.class, () -> productManagementUseCase.generateReport(searchProduct, context, dateRange, null));
        assertThrows(NullPointerException.class, () -> productManagementUseCase.generateReport(searchProduct, context, null, AvailableFormat.CSV));
        assertThrows(NullPointerException.class, () -> productManagementUseCase.generateReport(searchProduct, null, dateRange, AvailableFormat.CSV));
        assertThrows(NullPointerException.class, () -> productManagementUseCase.generateReport(null, context, dateRange, AvailableFormat.CSV));
        assertThrows(NullPointerException.class, () -> productManagementUseCase.generateReport(searchProduct, context, null, null));
        assertThrows(NullPointerException.class, () -> productManagementUseCase.generateReport(searchProduct, null, null, AvailableFormat.CSV));
        assertThrows(NullPointerException.class, () -> productManagementUseCase.generateReport(null, null, dateRange, AvailableFormat.CSV));
        assertThrows(NullPointerException.class, () -> productManagementUseCase.generateReport(searchProduct, null, dateRange, null));
        assertThrows(NullPointerException.class, () -> productManagementUseCase.generateReport(null, context, null, AvailableFormat.CSV));
        assertThrows(NullPointerException.class, () -> productManagementUseCase.generateReport(null, context, dateRange, null));
        assertThrows(NullPointerException.class, () -> productManagementUseCase.generateReport(searchProduct, null, null, null));
        assertThrows(NullPointerException.class, () -> productManagementUseCase.generateReport(null, null, null, AvailableFormat.CSV));
        assertThrows(NullPointerException.class, () -> productManagementUseCase.generateReport(null, null, dateRange, null));
        assertThrows(NullPointerException.class, () -> productManagementUseCase.generateReport(null, context, null, null));
        assertThrows(NullPointerException.class, () -> productManagementUseCase.generateReport(null, null, null, null));
    }
}
