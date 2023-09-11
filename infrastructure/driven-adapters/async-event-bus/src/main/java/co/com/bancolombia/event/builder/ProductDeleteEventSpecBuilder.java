package co.com.bancolombia.event.builder;

import co.com.bancolombia.d2b.model.events.AbstractEventSpec;
import co.com.bancolombia.event.dto.BodyDataProductDeleteDTO;
import co.com.bancolombia.event.dto.BodyDataProductDeleteFailureDTO;
import co.com.bancolombia.event.helper.ErrorBuildHelper;
import co.com.bancolombia.event.properties.EventNameProperties;
import co.com.bancolombia.model.commons.Context;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateway.ProductTypeAdapter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ProductDeleteEventSpecBuilder {

    private static final String CODE_RESPONSE = "204";
    private static final String MESSAGE_RESPONSE = "Successfully";
    private static final String TRANSACTION_STATE_DONE = "Revoked";
    private static final String TRANSACTION_STATE_FAILED = "Failed";
    public static final String TRANSACTION_OPERATION = "Delete";

    private final EventSpecBuilder eventSpecBuilder;
    private final EventNameProperties.Suffix eventSuffix;
    private final EventNameProperties.EventBusinessProduct eventBusinessProduct;
    private final ProductDeleteDTOBuilder productDeleteDTOBuilder;
    private final EventNameProperties eventNameProperties;
    private final ProductTypeAdapter productTypeAdapter;

    public ProductDeleteEventSpecBuilder(EventSpecBuilder eventSpecBuilder,
                                         EventNameProperties eventNameProperties,
                                         ProductDeleteDTOBuilder productDeleteDTOBuilder,
                                         ProductTypeAdapter productTypeAdapter) {
        this.eventSpecBuilder = eventSpecBuilder;
        this.eventBusinessProduct = eventNameProperties.getEventBusinessProduct();
        this.productDeleteDTOBuilder = productDeleteDTOBuilder;
        this.eventSuffix = eventBusinessProduct.getSuffix();
        this.eventNameProperties = eventNameProperties;
        this.productTypeAdapter = productTypeAdapter;
    }

    public Mono<AbstractEventSpec<BodyDataProductDeleteDTO>> buildProductRemoveSuccessSpec(
            Context context, Product product) {
        return productTypeAdapter.getProductTypes()
                .map(productTypes -> new BodyDataProductDeleteDTO(
                        MetaDTOBuilder.buildSuccessMeta(context, eventNameProperties.getTransactionCode(),
                                eventNameProperties.getTransactionCodeDescription()),
                        new BodyDataProductDeleteDTO.Request(productDeleteDTOBuilder
                                .productDeleteDTOBuilder(product, productTypes.get(product.getType())),
                                TRANSACTION_OPERATION),
                        new BodyDataProductDeleteDTO.Response(CODE_RESPONSE, MESSAGE_RESPONSE, TRANSACTION_STATE_DONE)))
                .map(eventBody -> eventSpecBuilder.buildEventSpec(context, eventSuffix.getRemoveProductDone(),
                        eventBusinessProduct.getProductPrefix(), eventBody));
    }

    public Mono<AbstractEventSpec<BodyDataProductDeleteFailureDTO>> buildProductRemoveFailureSpec(
            Context context, Product product, Throwable error) {
        return productTypeAdapter.getProductTypes()
                .map(productTypes -> new BodyDataProductDeleteFailureDTO(
                        MetaDTOBuilder.buildErrorMeta(context, eventNameProperties.getTransactionCode(),
                                eventNameProperties.getTransactionCodeDescription()),
                        new BodyDataProductDeleteFailureDTO
                                .Request(productDeleteDTOBuilder.productDeleteDTOBuilder(product,
                                productTypes.get(product.getType())), TRANSACTION_OPERATION),
                        new BodyDataProductDeleteFailureDTO.Response(ErrorBuildHelper
                                .getError(error, context), TRANSACTION_STATE_FAILED)))
                .map(eventBody -> eventSpecBuilder.buildEventSpec(context, eventSuffix.getRemoveProductRejected(),
                        eventBusinessProduct.getProductPrefix(), eventBody));
    }
}
