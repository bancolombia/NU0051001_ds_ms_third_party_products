package co.com.bancolombia.rest.parameters;

import co.com.bancolombia.exception.technical.TechnicalException;
import co.com.bancolombia.exception.technical.message.TechnicalErrorMessage;
import co.com.bancolombia.model.exception.BusinessException;
import co.com.bancolombia.model.parameters.ParameterAdapter;
import co.com.bancolombia.rest.commons.ActionsResponseBuilder;
import co.com.bancolombia.rest.commons.ClientRestUtil;
import co.com.bancolombia.rest.commons.HeadersBuilder;
import co.com.bancolombia.rest.commons.Request;
import co.com.bancolombia.rest.mapper.ParametersMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Component
@Repository
@EnableConfigurationProperties(ParameterProperties.class)
public class RestParameterAdapter implements ParameterAdapter {

    private final WebClient webClient;
    private final ParameterProperties parameterProperties;
    private static final String CHANNEL_QUERY_PARAM = "channelId";
    private static final String PARAMETER_NAME_QUERY_PARAM = "paramName";
    private static final String TRANSACTION_CODE_QUERY_PARAM = "transactionCode";
    private static final String ACTION = "GetParameters";
    private static final String EMPTY_BODY = "";

    public RestParameterAdapter(@Qualifier("d2bWebClient") WebClient webClient,
                                ParameterProperties parameterProperties) {
        this.webClient = webClient;
        this.parameterProperties = parameterProperties;
    }

    @Override
    public Mono<Map<String, String>> getProductTypes(String channel, String messageId) {

        var request = new Request<>(HeadersBuilder.buildHeaders(channel, messageId),
                buildBody(parameterProperties.getParameterName(), parameterProperties.getTransactionCode()),
                URI.create(parameterProperties.getParameterPath()));

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(parameterProperties.getParameterPath())
                        .queryParam(CHANNEL_QUERY_PARAM, channel)
                        .queryParam(PARAMETER_NAME_QUERY_PARAM, parameterProperties.getParameterName())
                        .queryParam(TRANSACTION_CODE_QUERY_PARAM, parameterProperties.getTransactionCode())
                        .build())
                .headers(HeadersBuilder.buildHeaders(request.getHeaders()))
                .exchangeToMono(response -> ClientRestUtil.getBodyResponse(ParametersResponseDTO.class,
                        request, response, ActionsResponseBuilder
                                .actionsResponseBuilder(ACTION, this.getClass().getName())))
                .map(ParametersMapper::toParametersMap)
                .timeout(Duration.ofMillis(parameterProperties.getTimeout()))
                .onErrorMap(TimeoutException.class, ex ->
                        new TechnicalException(ex, TechnicalErrorMessage.TIMEOUT_EXCEPTION))
                .onErrorMap(WebClientException.class, ex ->
                        new TechnicalException(ex, TechnicalErrorMessage.REST_CLIENT_EXCEPTION))
                .onErrorMap(ex -> (ex instanceof TechnicalException || ex instanceof BusinessException) ?
                        ex : new TechnicalException(ex, TechnicalErrorMessage.UNEXPECTED_EXCEPTION));
    }

    @Override
    public Mono<Map<String, String>> getDocumentTypes(String channel, String messageId) {

        var request = new Request<>(HeadersBuilder.buildHeaders(channel, messageId),
                EMPTY_BODY,
                URI.create(parameterProperties.getDocumentTypesPath()));

        return webClient.get()
                .uri(parameterProperties.getDocumentTypesPath())
                .headers(HeadersBuilder.buildHeaders(request.getHeaders()))
                .exchangeToMono(response -> ClientRestUtil.getBodyResponse(DocumentTypesResponseDTO.class,
                        request, response, ActionsResponseBuilder
                                .actionsResponseBuilder(ACTION, this.getClass().getName())))
                .map(ParametersMapper::toParametersMap)
                .timeout(Duration.ofMillis(parameterProperties.getTimeout()))
                .onErrorMap(TimeoutException.class, ex ->
                        new TechnicalException(ex, TechnicalErrorMessage.TIMEOUT_EXCEPTION))
                .onErrorMap(WebClientException.class, ex ->
                        new TechnicalException(ex, TechnicalErrorMessage.REST_CLIENT_EXCEPTION))
                .onErrorMap(ex -> (ex instanceof TechnicalException || ex instanceof BusinessException) ?
                        ex : new TechnicalException(ex, TechnicalErrorMessage.UNEXPECTED_EXCEPTION));
    }

    private static ParametersRequestDTO buildBody(String parametersName, String parameterCode) {
        return new ParametersRequestDTO(parametersName, parameterCode);
    }

}