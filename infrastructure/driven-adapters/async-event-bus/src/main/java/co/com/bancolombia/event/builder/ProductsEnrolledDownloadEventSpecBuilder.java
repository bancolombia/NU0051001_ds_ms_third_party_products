package co.com.bancolombia.event.builder;

import co.com.bancolombia.d2b.model.events.AbstractEventSpec;
import co.com.bancolombia.event.dto.BodyDataProductDownloadDTO;
import co.com.bancolombia.event.dto.BodyDataProductDownloadFailureDTO;
import co.com.bancolombia.event.helper.ErrorBuildHelper;
import co.com.bancolombia.event.properties.EventNameProperties;
import co.com.bancolombia.model.commons.Context;
import co.com.bancolombia.model.commons.DateRange;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.report.AvailableFormat;
import org.springframework.stereotype.Component;


/**
 * Spec Builder for the event of the Products Enrolled that were downloaded
 */
@Component
public class ProductsEnrolledDownloadEventSpecBuilder {

    private static final String REPORT_GENERATION_RESPONSE = "Report Generated Successfully";
    private static final String REPORT_GENERATION_CODE = "200";
    private static final String SEARCH_TRANSACTION_OPERATION = "Search";

    private final EventSpecBuilder eventSpecBuilder;
    private final EventNameProperties.Suffix eventSuffix;
    private final EventNameProperties.EventBusinessProduct eventBusinessProduct;

    private final SearchCriteriaDTOBuilder searchCriteriaDTOBuilder;
    private final EventNameProperties eventNameProperties;

    public ProductsEnrolledDownloadEventSpecBuilder(EventSpecBuilder eventSpecBuilder,
                                                    EventNameProperties eventNameProperties,
                                                    SearchCriteriaDTOBuilder searchCriteriaDTOBuilder) {
        this.eventSpecBuilder = eventSpecBuilder;
        this.eventBusinessProduct = eventNameProperties.getEventBusinessProduct();
        this.searchCriteriaDTOBuilder = searchCriteriaDTOBuilder;
        this.eventSuffix = eventBusinessProduct.getSuffix();
        this.eventNameProperties = eventNameProperties;

    }

    public AbstractEventSpec<BodyDataProductDownloadDTO> buildProductEnrolledDownloadSuccessSpec(
            Context context, Product product, DateRange dateRange, String filename, AvailableFormat format) {
        var eventBody = new BodyDataProductDownloadDTO(
                MetaDTOBuilder.buildSuccessMeta(context, eventNameProperties.getTransactionCode(),
                        eventNameProperties.getTransactionCodeDescription()),
                new BodyDataProductDownloadDTO.Request(
                        searchCriteriaDTOBuilder.buildSearchCriteriaDTO(product, dateRange),
                        format.getFormat(), SEARCH_TRANSACTION_OPERATION),
                new BodyDataProductDownloadDTO.Response(REPORT_GENERATION_CODE, filename, REPORT_GENERATION_RESPONSE));
        return eventSpecBuilder.buildEventSpec(context, eventSuffix.getProductsEnrolledDownloadDone(),
                eventBusinessProduct.getReportPrefix(), eventBody);
    }

    public AbstractEventSpec<BodyDataProductDownloadFailureDTO> buildProductEnrolledDownloadFailureSpec(
            Context context, Product product, DateRange dateRange, AvailableFormat format, Throwable error) {
        var eventBody = new BodyDataProductDownloadFailureDTO(
                MetaDTOBuilder.buildErrorMeta(context, eventNameProperties.getTransactionCode(),
                        eventNameProperties.getTransactionCodeDescription()),
                new BodyDataProductDownloadFailureDTO
                        .Request(searchCriteriaDTOBuilder.buildSearchCriteriaDTO(product, dateRange),
                        format.getFormat(), SEARCH_TRANSACTION_OPERATION),
                new BodyDataProductDownloadFailureDTO.Response(ErrorBuildHelper.getError(error, context))
        );
        return eventSpecBuilder.buildEventSpec(context, eventSuffix.getProductsEnrolledDownloadRejected(),
                eventBusinessProduct.getReportPrefix(), eventBody);
    }
}
