package co.com.bancolombia.model.product.gateway;

import co.com.bancolombia.model.commons.Context;
import co.com.bancolombia.model.user.Identification;
import reactor.core.publisher.Mono;

public interface ProductAccountValidationAdapter {

    Mono<Boolean> validateOwnership(String productNumber, String productType,
                                    Identification identification, Context context);
}
