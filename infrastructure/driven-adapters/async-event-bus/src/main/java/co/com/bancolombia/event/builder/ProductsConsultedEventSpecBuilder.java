package co.com.bancolombia.event.builder;

import co.com.bancolombia.d2b.model.events.AbstractEventSpec;
import co.com.bancolombia.event.dto.BodyDataProductSearchDTO;
import co.com.bancolombia.event.dto.BodyDataProductSearchFailureDTO;
import co.com.bancolombia.event.helper.ErrorBuildHelper;
import co.com.bancolombia.event.properties.EventNameProperties;
import co.com.bancolombia.model.commons.Context;
import co.com.bancolombia.model.commons.DateRange;
import co.com.bancolombia.model.product.Product;
import org.springframework.stereotype.Component;


/**
 * Spec Builder for the event of the Products Enrolled that were consulted
 */
@Component
public class ProductsConsultedEventSpecBuilder {

    public static final String SEARCH_TRANSACTION_OPERATION = "Search";
    public static final String SUCCESSFUL_CODE_RESPONSE = "200";
    public static final String SUCCESSFUL_MESSAGE_RESPONSE = "Successful";
    public static final String SUCCESSFUL_TRANSACTION_STATE = "Ok";
    public static final String FAILED_TRANSACTION_STATE = "Failed";

    private final EventSpecBuilder eventSpecBuilder;
    private final EventNameProperties.Suffix eventSuffix;
    private final EventNameProperties.EventBusinessProduct eventBusinessProduct;
    private final SearchCriteriaDTOBuilder searchCriteriaDTOBuilder;
    private final EventNameProperties eventNameProperties;

    public ProductsConsultedEventSpecBuilder(EventSpecBuilder eventSpecBuilder,
                                             EventNameProperties eventNameProperties,
                                             SearchCriteriaDTOBuilder searchCriteriaDTOBuilder) {
        this.eventSpecBuilder = eventSpecBuilder;
        this.eventBusinessProduct = eventNameProperties.getEventBusinessProduct();
        this.searchCriteriaDTOBuilder = searchCriteriaDTOBuilder;
        this.eventSuffix = eventBusinessProduct.getSuffix();
        this.eventNameProperties = eventNameProperties;
    }

    public AbstractEventSpec<BodyDataProductSearchDTO> buildSearchPaginatedProductsSuccessSpec(
            Context context, Product product, DateRange dateRange) {
        var eventBody = new BodyDataProductSearchDTO(
                MetaDTOBuilder.buildSuccessMeta(context, eventNameProperties.getTransactionCode(),
                        eventNameProperties.getTransactionCodeDescription()),
                new BodyDataProductSearchDTO.Request(
                        searchCriteriaDTOBuilder.buildSearchCriteriaDTO(product, dateRange),
                        SEARCH_TRANSACTION_OPERATION),
                new BodyDataProductSearchDTO.Response(SUCCESSFUL_CODE_RESPONSE, SUCCESSFUL_MESSAGE_RESPONSE,
                        SUCCESSFUL_TRANSACTION_STATE));
        return eventSpecBuilder.buildEventSpec(context, eventSuffix.getEnrolledProductsConsultedDone(),
                eventBusinessProduct.getProductPrefix(), eventBody);
    }

    public AbstractEventSpec<BodyDataProductSearchFailureDTO> buildSearchPaginatedProductsFailureSpec(
            Context context, Product product, DateRange dateRange, Throwable error) {
        var eventBody = new BodyDataProductSearchFailureDTO(
                MetaDTOBuilder.buildErrorMeta(context, eventNameProperties.getTransactionCode(),
                        eventNameProperties.getTransactionCodeDescription()),
                new BodyDataProductSearchFailureDTO.Request(
                        searchCriteriaDTOBuilder.buildSearchCriteriaDTO(product, dateRange),
                        SEARCH_TRANSACTION_OPERATION),
                new BodyDataProductSearchFailureDTO.Response(ErrorBuildHelper.getError(error, context),
                        FAILED_TRANSACTION_STATE));
        return eventSpecBuilder.buildEventSpec(context, eventSuffix.getEnrolledProductsConsultedRejected(),
                eventBusinessProduct.getProductPrefix(), eventBody);
    }
}