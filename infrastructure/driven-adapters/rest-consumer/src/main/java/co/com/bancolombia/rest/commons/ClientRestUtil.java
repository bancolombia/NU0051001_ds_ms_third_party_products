package co.com.bancolombia.rest.commons;

import co.com.bancolombia.exception.technical.TechnicalException;
import co.com.bancolombia.exception.technical.message.TechnicalErrorMessage;
import co.com.bancolombia.model.exception.BusinessException;
import co.com.bancolombia.rest.commons.logger.LoggerRest;
import co.com.bancolombia.rest.exception.ErrorResponseDTO;
import co.com.bancolombia.rest.exception.RestErrorMapper;
import lombok.experimental.UtilityClass;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@UtilityClass
public class ClientRestUtil {

    private final RestErrorMapper restErrorMapper = new RestErrorMapper();

    public <T> Mono<T> getBodyResponse(Class<T> expectedClassBody, Request<?> request,
                                       ClientResponse clientResponse, Map<String, String> actionsResponse) {
        return Mono.just(!clientResponse.statusCode().isError())
                .filter(Boolean.TRUE::equals)
                .flatMap(success -> getSuccessfulResponse(expectedClassBody, request, clientResponse, actionsResponse))
                .switchIfEmpty(getErrorResponse(request, clientResponse, actionsResponse));
    }

    public <T> Mono<T> getSuccessfulResponse(Class<T> expectedClassBody, Request<?> request,
                                             ClientResponse clientResponse, Map<String, String> actionsResponse) {
        Map<String, String> responseHeaders = clientResponse.headers().asHttpHeaders().toSingleValueMap();
        return clientResponse.bodyToMono(expectedClassBody)
                .flatMap(plainResponse -> {
                    buildLogMessage(request, responseHeaders, plainResponse, actionsResponse);
                    return Mono.just(plainResponse);
                });
    }

    public <T> Mono<T> getErrorResponse(Request<?> request, ClientResponse clientResponse,
                                        Map<String, String> actionsResponse) {
        Map<String, String> responseHeaders = clientResponse.headers().asHttpHeaders().toSingleValueMap();
        return clientResponse.bodyToMono(ErrorResponseDTO.class)
                .switchIfEmpty(Mono.defer(() -> Mono.error(buildTechnicalEmptyBodyException(clientResponse))))
                .flatMap(errorResponse -> {
                    buildLogMessage(request, responseHeaders, errorResponse, actionsResponse);
                    return Mono.just(errorResponse)
                            .map(errorResponseDTO -> errorResponseDTO.getErrors().get(0).getCode())
                            .flatMap(ClientRestUtil::throwException);
                });
    }

    private <T> Mono<T> throwException(String code) {
        if (restErrorMapper.getBusinessErrorMessage(code) != null) {
            return Mono.error(new BusinessException(restErrorMapper.getBusinessErrorMessage(code)));
        }
        return Mono.error(new TechnicalException(null, restErrorMapper.getTechnicalErrorMessage(code)));
    }

    public TechnicalException buildTechnicalEmptyBodyException(ClientResponse clientResponse) {
        return new TechnicalException(new Exception(clientResponse.statusCode().toString()),
                TechnicalErrorMessage.REST_CLIENT_EXCEPTION);
    }

    public <T> void buildLogMessage(Request<?> request, Map<String, String> responseHeaders,
                                    T response, Map<String, String> actionsResponse) {
        LoggerRest.logRequestResponse(request,
                new Response<>(responseHeaders, response),
                actionsResponse.get("action"), actionsResponse.get("service"));
    }
}
