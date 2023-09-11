package co.com.bancolombia.model.event;

import co.com.bancolombia.model.commons.Context;
import co.com.bancolombia.model.commons.DateRange;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.report.AvailableFormat;
import reactor.core.publisher.Mono;

public interface ProductManagementEventPublisher {

    Mono<Void> emitSuccessfulProductEnrollmentEvent(Product product, Context context);

    Mono<Void> emitFailedProductEnrollmentEvent(Product product, Context context, Throwable error);

    Mono<Void> emitSuccessfulProductModifyEvent(Product product, Context context);

    Mono<Void> emitFailedProductModifyEvent(Product product, Context context, Throwable error);

    Mono<Void> emitSuccessfulSearchPaginatedProductsEvent(Context context, Product product, DateRange dateRange);

    Mono<Void> emitFailedSearchPaginatedProductsEvent(Context context, Product product, DateRange dateRange,
                                                      Throwable error);

    Mono<Void> emitSuccessfulDeleteProductEvent(Product product, Context context);

    Mono<Void> emitFailedDeleteProductEvent(Product product, Context context, Throwable error);

    Mono<Void> emitSuccessfulProductEnrolledDownloadEvent(Context context, Product product,
                                                          DateRange dateRange, String filename,
                                                          AvailableFormat format);

    Mono<Void> emitFailedProductEnrolledDownloadEvent(Context context, Product product,
                                                      DateRange dateRange, AvailableFormat format,
                                                      Throwable error);

}