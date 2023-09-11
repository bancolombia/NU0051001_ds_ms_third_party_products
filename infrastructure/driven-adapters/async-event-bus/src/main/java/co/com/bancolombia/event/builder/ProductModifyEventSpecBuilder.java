package co.com.bancolombia.event.builder;

import co.com.bancolombia.d2b.model.events.AbstractEventSpec;
import co.com.bancolombia.event.commons.BodyDataNotificationDTO;
import co.com.bancolombia.event.dto.BodyDataProductModifyDTO;
import co.com.bancolombia.event.dto.BodyDataProductModifyFailureDTO;
import co.com.bancolombia.event.helper.ErrorBuildHelper;
import co.com.bancolombia.event.properties.EventNameProperties;
import co.com.bancolombia.event.properties.NotificationProperties;
import co.com.bancolombia.model.commons.Context;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateway.ProductTypeAdapter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@EnableConfigurationProperties(NotificationProperties.class)
public class ProductModifyEventSpecBuilder {

    private static final String CODE_RESPONSE = "200";
    private static final String MESSAGE_RESPONSE = "Successfully";
    private static final String TRANSACTION_STATE_DONE = "Active";
    private static final String TRANSACTION_STATE_FAILED = "Failed";
    public static final String TRANSACTION_OPERATION = "Modify";

    private final EventSpecBuilder eventSpecBuilder;
    private final EventNameProperties.Suffix eventSuffix;
    private final EventNameProperties.EventBusinessProduct eventBusinessProduct;
    private final NotificationProperties notificationProperties;
    private final ProductModifyDTOBuilder productModifyDTOBuilder;
    private final ProductTypeAdapter productTypeAdapter;
    private final EventNameProperties eventNameProperties;

    public ProductModifyEventSpecBuilder(EventSpecBuilder eventSpecBuilder,
                                         EventNameProperties eventNameProperties,
                                         NotificationProperties notification,
                                         ProductModifyDTOBuilder productModifyDTOBuilder,
                                         ProductTypeAdapter productTypeAdapter) {
        this.eventSpecBuilder = eventSpecBuilder;
        this.eventBusinessProduct = eventNameProperties.getEventBusinessProduct();
        this.productModifyDTOBuilder = productModifyDTOBuilder;
        this.eventSuffix = eventBusinessProduct.getSuffix();
        this.notificationProperties = notification;
        this.productTypeAdapter = productTypeAdapter;
        this.eventNameProperties = eventNameProperties;

    }

    public Mono<AbstractEventSpec<BodyDataProductModifyDTO>> buildProductModifySuccessSpec(
            Context context, Product product) {
        return productTypeAdapter.getProductTypes()
                .map(productTypes -> new BodyDataProductModifyDTO(
                        MetaDTOBuilder.buildSuccessMeta(context, eventNameProperties.getTransactionCode(),
                                eventNameProperties.getTransactionCodeDescription()),
                        new BodyDataProductModifyDTO.Request(productModifyDTOBuilder
                                .productModifyDTOBuilder(product,
                                        productTypes.get(product.getType())), TRANSACTION_OPERATION),
                        new BodyDataProductModifyDTO.Response(CODE_RESPONSE, MESSAGE_RESPONSE, TRANSACTION_STATE_DONE),
                        BodyDataNotificationDTO.buildBodyDataNotificationDTO(notificationProperties,
                                notificationProperties.productModificationTransactionAlertCode())))
                .map(eventBody -> eventSpecBuilder.buildEventSpec(context, eventSuffix.getModificationProductDone(),
                        eventBusinessProduct.getProductPrefix(), eventBody));
    }

    public Mono<AbstractEventSpec<BodyDataProductModifyFailureDTO>> buildProductModifyFailureSpec(
            Context context, Product product, Throwable error) {
        return productTypeAdapter.getProductTypes()
                .map(productTypes -> new BodyDataProductModifyFailureDTO(
                        MetaDTOBuilder.buildErrorMeta(context, eventNameProperties.getTransactionCode(),
                                eventNameProperties.getTransactionCodeDescription()),
                        new BodyDataProductModifyFailureDTO
                                .Request(productModifyDTOBuilder
                                .productModifyDTOBuilder(product,
                                        productTypes.get(product.getType())), TRANSACTION_OPERATION),
                        new BodyDataProductModifyFailureDTO.Response(ErrorBuildHelper
                                .getError(error, context), TRANSACTION_STATE_FAILED)))
                .map(eventBody -> eventSpecBuilder.buildEventSpec(context, eventSuffix.getModificationProductRejected(),
                        eventBusinessProduct.getProductPrefix(), eventBody));
    }
}
