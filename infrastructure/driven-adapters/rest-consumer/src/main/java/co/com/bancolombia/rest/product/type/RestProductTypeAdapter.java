package co.com.bancolombia.rest.product.type;

import co.com.bancolombia.d2b.model.cache.FunctionalCacheOps;
import co.com.bancolombia.exception.technical.TechnicalException;
import co.com.bancolombia.exception.technical.message.TechnicalErrorMessage;
import co.com.bancolombia.model.product.gateway.ProductTypeAdapter;
import co.com.bancolombia.rest.parameters.RestParameterAdapter;
import co.com.bancolombia.rest.product.properties.RavenHomologationProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RestProductTypeAdapter implements ProductTypeAdapter {

    private final RavenHomologationProperties ravenHomologationProperties;
    private final RestParameterAdapter restParameterAdapter;
    private final FunctionalCacheOps<Map<String, String>> cache;
    private final String channel;

    public static final int FIRST_ELEMENT = 0;
    public static final String PRODUCT_TYPES_CACHE_KEY = "productTypes";

    public RestProductTypeAdapter(RestParameterAdapter restParameterAdapter,
                                  @Value("${adapter.channel}") String channel,
                                  RavenHomologationProperties ravenHomologationProperties,
                                  FunctionalCacheOps<Map<String, String>> productTypeCache) {
        this.restParameterAdapter = restParameterAdapter;
        this.channel = channel;
        this.ravenHomologationProperties = ravenHomologationProperties;
        this.cache = productTypeCache;
    }

    public Mono<Map<String, String>> getProductTypes() {
        return cache.getFromCache(PRODUCT_TYPES_CACHE_KEY, () ->
                        restParameterAdapter.getProductTypes(channel, ""))
                .onErrorMap(exception ->
                        new TechnicalException(exception, TechnicalErrorMessage.ERROR_GETTING_PRODUCT_TYPES));
    }

    public List<String> getIdentificationTypes() {
        return ravenHomologationProperties.getHomologations()
                .get(FIRST_ELEMENT).data().stream().map(RavenHomologationProperties.Data::internalContextValue)
                .collect(Collectors.toList());
    }
}


