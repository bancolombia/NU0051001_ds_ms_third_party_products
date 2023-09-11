package co.com.bancolombia.api.validations;

import co.com.bancolombia.api.commons.ContextBuilder;
import co.com.bancolombia.api.commons.RequestHelper;
import co.com.bancolombia.api.commons.enums.RequiredPathVariable;
import co.com.bancolombia.api.commons.enums.RequiredQueryParam;
import co.com.bancolombia.api.validations.mapper.ProductValidationMapper;
import co.com.bancolombia.model.user.Identification;
import co.com.bancolombia.model.user.gateway.CustomerAdapter;
import co.com.bancolombia.usecase.product.validations.ProductAccountValidationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.OK;

@Configuration
@RequiredArgsConstructor
public class ProductValidationHandler {

    private final ProductAccountValidationUseCase productAccountValidationUseCase;
    private final CustomerAdapter customerAdapter;
    private final ContextBuilder contextBuilder;


    public Mono<ServerResponse> validateOwnership(ServerRequest serverRequest) {
        var productNumber = RequestHelper.getPathVariableByNumericContent(serverRequest,
                RequiredPathVariable.PRODUCT_NUMBER);
        var productType = RequestHelper.getQueryParam(serverRequest, RequiredQueryParam.PRODUCT_TYPE);
        var identificationType = RequestHelper.getQueryParam(serverRequest, RequiredQueryParam.IDENTIFICATION_TYPE);
        var identificationNumber = RequestHelper.getQueryParam(serverRequest, RequiredQueryParam.IDENTIFICATION_NUMBER);
        var identification = new Identification(identificationType, identificationNumber);

        return contextBuilder.getContext(serverRequest)
                .flatMap(context -> productAccountValidationUseCase
                        .validateOwnership(productNumber, productType, identification, context)
                        .then(customerAdapter.getCustomerName(identification, context)))
                .map(ProductValidationMapper::mapToOwnershipValidationResponse)
                .flatMap(ownershipValidationResponseDTO -> ServerResponse.status(OK)
                        .bodyValue(ownershipValidationResponseDTO));
    }
}