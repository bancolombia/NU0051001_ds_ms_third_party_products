package co.com.bancolombia.model.parameters;

import reactor.core.publisher.Mono;

import java.util.Map;

public interface ParameterAdapter {

    Mono<Map<String, String>> getProductTypes(String channel, String messageId);

    Mono<Map<String, String>> getDocumentTypes(String channel, String messageId);
}