package co.com.bancolombia.model.product.gateway;

import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface ProductTypeAdapter {

    Mono<Map<String, String>> getProductTypes();

    List<String> getIdentificationTypes();
}
