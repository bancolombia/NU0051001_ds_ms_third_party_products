package co.com.bancolombia.event;

import co.com.bancolombia.d2b.model.events.AbstractEventSpec;
import co.com.bancolombia.d2b.model.events.IEventPublisherGateway;
import co.com.bancolombia.event.builder.ProductDeleteEventSpecBuilder;
import co.com.bancolombia.event.builder.ProductEnrollEventSpecBuilder;
import co.com.bancolombia.event.builder.ProductModifyEventSpecBuilder;
import co.com.bancolombia.event.builder.ProductsConsultedEventSpecBuilder;
import co.com.bancolombia.event.builder.ProductsEnrolledDownloadEventSpecBuilder;
import co.com.bancolombia.exception.technical.TechnicalException;
import co.com.bancolombia.exception.technical.message.TechnicalErrorMessage;
import co.com.bancolombia.model.commons.Context;
import co.com.bancolombia.model.commons.DateRange;
import co.com.bancolombia.model.event.ProductManagementEventPublisher;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.report.AvailableFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class EventTransmitter implements ProductManagementEventPublisher {

    private final IEventPublisherGateway eventPublisherGateway;
    private final ProductEnrollEventSpecBuilder productEnrollEventSpecBuilder;
    private final ProductsEnrolledDownloadEventSpecBuilder productsEnrolledDownloadEventSpecBuilder;
    private final ProductsConsultedEventSpecBuilder productsConsultedEventSpecBuilder;
    private final ProductDeleteEventSpecBuilder productDeleteEventSpecBuilder;
    private final ProductModifyEventSpecBuilder productModifyEventSpecBuilder;

    private final String downloadProductsRoute;

    public EventTransmitter(IEventPublisherGateway eventPublisherGateway,
                            ProductEnrollEventSpecBuilder productEnrollEventSpecBuilder,
                            ProductsEnrolledDownloadEventSpecBuilder productsEnrolledDownloadEventSpecBuilder,
                            ProductsConsultedEventSpecBuilder productsConsultedEventSpecBuilder,
                            ProductDeleteEventSpecBuilder productDeleteEventSpecBuilder,
                            ProductModifyEventSpecBuilder productModifyEventSpecBuilder,
                            @Value("${routes.path-mapping.product.download}") String downloadProductsRoute) {
        this.eventPublisherGateway = eventPublisherGateway;
        this.productEnrollEventSpecBuilder = productEnrollEventSpecBuilder;
        this.productsEnrolledDownloadEventSpecBuilder = productsEnrolledDownloadEventSpecBuilder;
        this.productsConsultedEventSpecBuilder = productsConsultedEventSpecBuilder;
        this.productDeleteEventSpecBuilder = productDeleteEventSpecBuilder;
        this.downloadProductsRoute = downloadProductsRoute;
        this.productModifyEventSpecBuilder = productModifyEventSpecBuilder;
    }

    @Override
    public Mono<Void> emitSuccessfulProductEnrollmentEvent(Product product, Context context) {
        productEnrollEventSpecBuilder.buildProductEnrollSuccessSpec(context, product)
                .flatMap(this::emitEvent)
                .subscribe();
        return Mono.empty();
    }

    @Override
    public Mono<Void> emitFailedProductEnrollmentEvent(Product product, Context context, Throwable error) {
        productEnrollEventSpecBuilder
                .buildProductEnrollFailedSpec(context, product, error)
                .flatMap(this::emitEvent)
                .subscribe();
        return Mono.empty();
    }

    @Override
    public Mono<Void> emitSuccessfulProductModifyEvent(Product product, Context context) {
        productModifyEventSpecBuilder.buildProductModifySuccessSpec(context, product)
                .flatMap(this::emitEvent)
                .subscribe();
        return Mono.empty();
    }

    @Override
    public Mono<Void> emitFailedProductModifyEvent(Product product, Context context, Throwable error) {
        productModifyEventSpecBuilder.buildProductModifyFailureSpec(context, product, error)
                .flatMap(this::emitEvent)
                .subscribe();
        return Mono.empty();
    }

    @Override
    public Mono<Void> emitSuccessfulSearchPaginatedProductsEvent(Context context, Product product,
                                                                 DateRange dateRange) {
        var eventData = productsConsultedEventSpecBuilder.buildSearchPaginatedProductsSuccessSpec(
                context, product, dateRange);
        if (shouldEmitSearchPaginatedProductsEvent(context)) {
            emitEvent(eventData).subscribe();
        }
        return Mono.empty();
    }

    @Override
    public Mono<Void> emitFailedSearchPaginatedProductsEvent(Context context, Product product, DateRange dateRange,
                                                             Throwable error) {
        var eventData = productsConsultedEventSpecBuilder.buildSearchPaginatedProductsFailureSpec(
                context, product, dateRange, error);
        if (shouldEmitSearchPaginatedProductsEvent(context)) {
            emitEvent(eventData).subscribe();
        }
        return Mono.empty();
    }

    public boolean shouldEmitSearchPaginatedProductsEvent(Context context) {
        return !context.getDomain().equals(downloadProductsRoute);
    }

    @Override
    public Mono<Void> emitSuccessfulProductEnrolledDownloadEvent(Context context, Product product,
                                                                 DateRange dateRange, String filename,
                                                                 AvailableFormat format) {
        var eventData =
                productsEnrolledDownloadEventSpecBuilder
                        .buildProductEnrolledDownloadSuccessSpec(context, product, dateRange, filename, format);
        return emitEvent(eventData);
    }

    @Override
    public Mono<Void> emitFailedProductEnrolledDownloadEvent(Context context, Product product,
                                                             DateRange dateRange, AvailableFormat format,
                                                             Throwable error) {
        var eventData =
                productsEnrolledDownloadEventSpecBuilder
                        .buildProductEnrolledDownloadFailureSpec(context, product, dateRange, format, error);
        return emitEvent(eventData);
    }

    @Override
    public Mono<Void> emitSuccessfulDeleteProductEvent(Product product, Context context) {
        productDeleteEventSpecBuilder.buildProductRemoveSuccessSpec(context, product)
                .flatMap(this::emitEvent)
                .subscribe();
        return Mono.empty();
    }

    @Override
    public Mono<Void> emitFailedDeleteProductEvent(Product product, Context context, Throwable error) {
        productDeleteEventSpecBuilder.buildProductRemoveFailureSpec(context, product, error)
                .flatMap(this::emitEvent)
                .subscribe();
        return Mono.empty();
    }

    Mono<Void> emitEvent(AbstractEventSpec<?> eventData) {
        return eventPublisherGateway.emitEvent(eventData)
                .onErrorMap(Exception.class,
                        error -> new TechnicalException(error, TechnicalErrorMessage.ERROR_EMITTING_EVENT));
    }
}
