package co.com.bancolombia.model.user.gateway;

import co.com.bancolombia.model.commons.Context;
import co.com.bancolombia.model.user.Identification;
import reactor.core.publisher.Mono;

public interface CustomerAdapter {

    Mono<String> getCustomerName(Identification identification, Context context);
}
