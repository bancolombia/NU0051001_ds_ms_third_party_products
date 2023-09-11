package co.com.bancolombia.usecase.product;

import co.com.bancolombia.model.commons.Context;
import co.com.bancolombia.model.commons.DateRange;
import co.com.bancolombia.model.commons.Function;
import co.com.bancolombia.model.commons.PageRequest;
import co.com.bancolombia.model.commons.PageSummary;
import co.com.bancolombia.model.event.ProductManagementEventPublisher;
import co.com.bancolombia.model.exception.BusinessException;
import co.com.bancolombia.model.exception.message.BusinessErrorMessage;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.factory.ProductFactory;
import co.com.bancolombia.model.product.gateway.ProductManager;
import co.com.bancolombia.model.product.gateway.ProductRepository;
import co.com.bancolombia.model.product.report.AvailableFormat;
import co.com.bancolombia.model.product.report.ProductsReportAdapter;
import co.com.bancolombia.model.user.Customer;
import co.com.bancolombia.usecase.product.validations.ProductValidationUseCase;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.Set;

@RequiredArgsConstructor
public class ProductManagementUseCase {

    public static final int PAGE_NUMBER = 1;
    private final ProductUseCase productUseCase;
    private final AffiliateProductUseCase affiliateProductUseCase;
    private final ProductValidationUseCase productValidation;
    private final ProductRepository productRepository;
    private final ProductManagementEventPublisher eventPublisher;
    private final ProductsReportAdapter productsReportAdapter;
    private static final Integer PAGE_OFFSET = PAGE_NUMBER;

    public Mono<Product> enrollProduct(@NonNull Product product, @NonNull Context context) {
        return getProductManager(product)
                .doOnError(error ->
                        eventPublisher.emitFailedProductEnrollmentEvent(product, context, error))
                .flatMap(
                    productManager -> productManager.validateProduct(product, context)
                        .flatMap(isValidated -> productManager.enrollProduct(product, context))
                        .flatMap(productRepository::enrollProduct))
                .doOnSuccess(enrolledProduct ->
                        eventPublisher.emitSuccessfulProductEnrollmentEvent(product, context))
                .doOnError(error ->
                        eventPublisher.emitFailedProductEnrollmentEvent(product, context, error));
    }


    public Mono<Product> modifyProduct(@NonNull String productId, String name,
                                       Set<Function> functions, @NonNull Context context) {

        return productRepository.getProduct(context.getCustomer(), productId)
                .switchIfEmpty(Mono.defer(() -> Mono.error(
                        new BusinessException(BusinessErrorMessage.PRODUCT_NOT_EXIST))))
                .doOnError(error ->
                        eventPublisher.emitFailedProductModifyEvent(
                                Product.builder().id(productId).build(), context, error))
                .flatMap(currentProduct -> validations(currentProduct, name, context.getCustomer())
                        .filter(Boolean.TRUE::equals)
                        .map(result -> productValidation.isNewInformationProduct(currentProduct, name, functions))
                        .filter(Boolean.TRUE::equals)
                        .flatMap(result -> getProductManager(currentProduct)
                                .flatMap(productManager -> productManager.modifyProduct(
                                        currentProduct,
                                        createNewProduct(currentProduct, name, functions),
                                        context))
                                )
                        .flatMap(newProduct -> productRepository.modifyProduct(context.getCustomer(), newProduct))
                        .doOnSuccess(productResult ->
                                eventPublisher.emitSuccessfulProductModifyEvent(productResult, context))
                        .doOnError(error ->
                                eventPublisher.emitFailedProductModifyEvent(currentProduct, context, error)));
    }


    public Mono<Boolean> deleteProduct(@NonNull Context context, @NonNull String productId) {

        return productRepository.getProduct(context.getCustomer(), productId)
                .switchIfEmpty(Mono.defer(() -> Mono.error(
                        new BusinessException(BusinessErrorMessage.PRODUCT_NOT_EXIST))))
                .doOnError(error ->
                        eventPublisher.emitFailedDeleteProductEvent(Product.builder().id(productId).build(),
                                context, error))
                .flatMap(product -> productRepository.deleteProduct(context.getCustomer(), productId)
                        .map(Boolean.TRUE::equals)
                        .doOnSuccess(result ->
                                eventPublisher.emitSuccessfulDeleteProductEvent(product, context))
                        .doOnError(error -> eventPublisher.emitFailedDeleteProductEvent(product, context, error)));

    }

    public Mono<ProductManager> getProductManager(Product product) {
        return productValidation.isProductEntityFromTheBank(product)
                .flatMap(isProductEntityFromTheBank ->
                        Boolean.TRUE.equals(isProductEntityFromTheBank) ? Mono.just(productUseCase) :
                                Mono.just(affiliateProductUseCase));
    }

    public Product createNewProduct(Product product, String customName, Set<Function> functions) {
        var newProduct = ProductFactory.newInstance(product);
        if (customName != null) {
            newProduct.setName(customName);
        }
        if (functions != null ) {
            newProduct.setFunctionList(functions);
        }
        return newProduct;
    }

    public Mono<Boolean> validations(Product product, String customName, Customer customer) {
        return productValidation.isProductTypeAuthorized(product)
                .filter(Boolean.TRUE::equals)
                .flatMap(result -> productRepository.isCustomNameAvailable(customer, customName)
                        .filter(Boolean.TRUE::equals)
                        .switchIfEmpty(Mono.defer(() -> Mono.error(
                                new BusinessException(BusinessErrorMessage.CUSTOM_NAME_UNAVAILABLE)))));
    }


    public Mono<PageSummary<Product>> searchPaginatedProducts(@NonNull Product product,
                                                              @NonNull PageRequest pageRequest,
                                                              @NonNull Context context,
                                                              @NonNull DateRange dateRange) {
        return productRepository.searchPaginatedProducts(product, pageRequest, context, dateRange)
                .filter(pageSummary -> !pageSummary.getData().isEmpty())
                .switchIfEmpty(Mono.defer(() ->
                        Mono.error(new BusinessException(BusinessErrorMessage.NON_PRODUCTS_FOUND))))
                .doOnSuccess(res ->
                        eventPublisher.emitSuccessfulSearchPaginatedProductsEvent(context, product, dateRange))
                .doOnError(error ->
                        eventPublisher.emitFailedSearchPaginatedProductsEvent(context, product, dateRange, error));
    }

    public Mono<Void> generateReport(@NonNull Product product,
                                     @NonNull Context context,
                                     DateRange dateRange,
                                     @NonNull AvailableFormat format) {
        var pageRequest = new PageRequest(PAGE_NUMBER);
        return searchPaginatedProducts(product, pageRequest, context, dateRange)
                .map(pageSummary ->
                        Flux.range(pageRequest.getNumber() + PAGE_OFFSET,
                                        pageSummary.getTotalPages() - PAGE_OFFSET)
                                .flatMap(pageNumber -> productRepository
                                        .searchPaginatedProducts(product, new PageRequest(pageNumber),
                                                context, dateRange))
                                .mergeWith(Flux.just(pageSummary))
                                .map(PageSummary::getData)
                                .collect(ArrayList<Product>::new, ArrayList::addAll)
                                .flatMap(products -> productsReportAdapter.generateReport(products, format))
                                .flatMap(bytes -> productsReportAdapter.uploadReport(bytes, context, format))
                                .doOnSuccess(filename -> eventPublisher.
                                        emitSuccessfulProductEnrolledDownloadEvent(context, product, dateRange,
                                                filename, format).subscribe())
                                .doOnError(error -> eventPublisher.
                                        emitFailedProductEnrolledDownloadEvent(context, product, dateRange,
                                                format, error).subscribe())
                                .subscribeOn(Schedulers.boundedElastic())
                                .subscribe()
                )
                .then()
                .doOnError(error -> eventPublisher.
                        emitFailedProductEnrolledDownloadEvent(context, product, dateRange, format, error).subscribe());
    }
}
