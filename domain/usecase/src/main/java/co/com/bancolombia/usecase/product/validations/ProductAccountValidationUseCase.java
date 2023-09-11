package co.com.bancolombia.usecase.product.validations;

import co.com.bancolombia.model.commons.Context;
import co.com.bancolombia.model.exception.BusinessException;
import co.com.bancolombia.model.exception.message.BusinessErrorMessage;
import co.com.bancolombia.model.product.gateway.ProductAccountValidationAdapter;
import co.com.bancolombia.model.user.Identification;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ProductAccountValidationUseCase {

    private final ProductAccountValidationAdapter productAccountValidationAdapter;

    public Mono<Boolean> validateOwnership(@NonNull String productNumber, @NonNull String productType,
                                           @NonNull Identification identification, @NonNull Context context) {
        return productAccountValidationAdapter.validateOwnership(productNumber, productType, identification, context)
                .filter(Boolean.TRUE::equals)
                .switchIfEmpty(Mono.defer(() ->
                        Mono.error(new BusinessException(BusinessErrorMessage.INVALID_OWNERSHIP))));
    }
}
