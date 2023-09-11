package co.com.bancolombia.event.builder;

import co.com.bancolombia.d2b.model.events.AbstractEventSpec;
import co.com.bancolombia.event.commons.BeneficiaryDTO;
import co.com.bancolombia.event.commons.FunctionDTO;
import co.com.bancolombia.event.commons.ProductDTO;
import co.com.bancolombia.event.commons.ProductRequestDTO;
import co.com.bancolombia.event.commons.ProductTypeDTO;
import co.com.bancolombia.event.dto.BodyDataProductEnrollmentDTO;
import co.com.bancolombia.event.dto.BodyDataProductEnrollmentFailureDTO;
import co.com.bancolombia.event.helper.ErrorBuildHelper;
import co.com.bancolombia.event.properties.EventNameProperties;
import co.com.bancolombia.event.properties.NotificationProperties;
import co.com.bancolombia.model.commons.Context;
import co.com.bancolombia.model.commons.Function;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateway.ProductTypeAdapter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.stream.Collectors;

import static co.com.bancolombia.event.commons.BodyDataNotificationDTO.buildBodyDataNotificationDTO;

@Component
public class ProductEnrollEventSpecBuilder {

    private static final String CODE_RESPONSE = "201";
    private static final String MESSAGE_RESPONSE = "Successfully";
    private static final String TRANSACTION_STATE_FAILED = "Failed";
    public static final String TRANSACTION_OPERATION = "Enroll";

    private final EventSpecBuilder eventSpecBuilder;
    private final EventNameProperties.Suffix eventSuffix;
    private final EventNameProperties.EventBusinessProduct eventBusinessProduct;
    private final NotificationProperties notificationProperties;
    private final ProductTypeAdapter productTypeAdapter;
    private final EventNameProperties eventNameProperties;

    public ProductEnrollEventSpecBuilder(EventSpecBuilder eventSpecBuilder, EventNameProperties eventNameProperties,
                                         NotificationProperties notificationProperties,
                                         ProductTypeAdapter productTypeAdapter) {
        this.eventSpecBuilder = eventSpecBuilder;
        this.eventBusinessProduct = eventNameProperties.getEventBusinessProduct();
        this.eventSuffix = eventBusinessProduct.getSuffix();
        this.notificationProperties = notificationProperties;
        this.productTypeAdapter = productTypeAdapter;
        this.eventNameProperties = eventNameProperties;
    }

    public Mono<AbstractEventSpec<BodyDataProductEnrollmentDTO>> buildProductEnrollSuccessSpec(Context context,
                                                                                               Product product) {
        return productTypeAdapter.getProductTypes()
                .map(productTypes -> new BodyDataProductEnrollmentDTO(
                        MetaDTOBuilder.buildSuccessMeta(context, eventNameProperties.getTransactionCode(),
                                eventNameProperties.getTransactionCodeDescription()),
                        buildRequest(product, productTypes),
                        new BodyDataProductEnrollmentDTO.Response(CODE_RESPONSE, MESSAGE_RESPONSE, product.getState()),
                        buildBodyDataNotificationDTO(notificationProperties,
                                notificationProperties.productEnrollmentTransactionAlertCode())))
                .map(eventBody -> eventSpecBuilder.buildEventSpec(context, eventSuffix.getEnrollProductDone(),
                        eventBusinessProduct.getProductPrefix(), eventBody));
    }

    public Mono<AbstractEventSpec<BodyDataProductEnrollmentFailureDTO>> buildProductEnrollFailedSpec(Context context,
                                                                                                     Product product,
                                                                                                     Throwable error) {
        return productTypeAdapter.getProductTypes()
                .map(productTypes -> new BodyDataProductEnrollmentFailureDTO(
                        MetaDTOBuilder.buildErrorMeta(context, eventNameProperties.getTransactionCode(),
                                eventNameProperties.getTransactionCodeDescription()),
                        buildRequest(product, productTypes),
                        new BodyDataProductEnrollmentFailureDTO.Response(ErrorBuildHelper.getError(error, context),
                                TRANSACTION_STATE_FAILED)))
                .map(eventBody -> eventSpecBuilder.buildEventSpec(context, eventSuffix.getEnrollProductRejected(),
                        eventBusinessProduct.getProductPrefix(), eventBody));
    }

    private ProductRequestDTO buildRequest(Product product, Map<String, String> productTypes) {
        return new ProductRequestDTO(
                new ProductDTO(product.getId(), product.getName(), product.getEntity(),
                        new ProductTypeDTO(product.getType(), productTypes.get(product.getType())), product.getNumber(),
                        new BeneficiaryDTO(product.getBeneficiary().getName(),
                                new BeneficiaryDTO.IdentificationDTO(
                                        product.getBeneficiary().getIdentification().type(),
                                        product.getBeneficiary().getIdentification().number()))
                        , product.getFunctionList().stream()
                        .map(Function::getProductFunction)
                        .map(FunctionDTO::new)
                        .collect(Collectors.toList()))
                , TRANSACTION_OPERATION);
    }
}
