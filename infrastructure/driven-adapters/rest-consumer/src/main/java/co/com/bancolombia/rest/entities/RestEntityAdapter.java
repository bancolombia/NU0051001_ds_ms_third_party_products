package co.com.bancolombia.rest.entities;

import co.com.bancolombia.exception.technical.TechnicalException;
import co.com.bancolombia.exception.technical.message.TechnicalErrorMessage;
import co.com.bancolombia.model.exception.BusinessException;
import co.com.bancolombia.rest.commons.ActionsResponseBuilder;
import co.com.bancolombia.rest.commons.ClientRestUtil;
import co.com.bancolombia.rest.commons.HeadersBuilder;
import co.com.bancolombia.rest.commons.Request;
import co.com.bancolombia.rest.config.RestConsumerProperties;
import co.com.bancolombia.rest.entities.dto.BankEntitiesResponseDTO;
import co.com.bancolombia.rest.mapper.BankEntitiesMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

@Component
public class RestEntityAdapter {

    private final WebClient webClient;
    private final RestConsumerProperties.AchProperties achProperties;
    private static final String ACTION = "GetBankCodes";
    private static final String PARAMETER_PAGE_SIZE_NAME = "pageSize";
    private static final String PARAMETER_PAGE_NUMBER_NAME = "pageNumber";
    private static final Integer PARAMETER_PAGE_SIZE_VALUE = 70;
    private static final Integer PARAMETER_PAGE_NUMBER_VALUE = 1;
    private static final String EMPTY_BODY = "";
    private static final Integer FIRST_ELEMENT = 1;
    private static final Integer ZERO = 0;
    private static final Pattern REGEX_PATTERN = Pattern.compile("pageNumber=(\\d+)");

    public RestEntityAdapter(@Qualifier("apicWebClient") WebClient webClient,
                             RestConsumerProperties.AchProperties achProperties) {
        this.webClient = webClient;
        this.achProperties = achProperties;
    }

    public Mono<Integer> getTotalPages(String channel) {
        return restBankRequest(channel, PARAMETER_PAGE_NUMBER_VALUE)
                .flatMap(result -> {
                    var pageNumberMatcher = REGEX_PATTERN.matcher(result.links().last());
                    return pageNumberMatcher.find() ?
                            Mono.just(Integer.parseInt(pageNumberMatcher.group(FIRST_ELEMENT))) : Mono.just(ZERO);
                })
                .onErrorMap(ex -> new TechnicalException(ex, TechnicalErrorMessage.UNEXPECTED_EXCEPTION));
    }

    public Mono<Set<String>> getBankEntities(String channel, Integer pageNumber) {
        return restBankRequest(channel, pageNumber)
                .map(BankEntitiesMapper::toBankEntitiesSet)
                .onErrorMap(ex -> new TechnicalException(ex, TechnicalErrorMessage.UNEXPECTED_EXCEPTION));
    }

    protected Mono<BankEntitiesResponseDTO> restBankRequest(String channel, Integer parameterPageNumberValue) {
        var request = new Request<>(HeadersBuilder.buildHeaders(channel, UUID.randomUUID().toString()),
                EMPTY_BODY, URI.create(achProperties.getListBanksUrl()));

        return webClient.get()
                .uri(UriComponentsBuilder.fromHttpUrl(achProperties.getBaseUrl())
                        .path(achProperties.getListBankEntities())
                        .queryParam(PARAMETER_PAGE_SIZE_NAME, PARAMETER_PAGE_SIZE_VALUE)
                        .queryParam(PARAMETER_PAGE_NUMBER_NAME, parameterPageNumberValue)
                        .build().toUri())
                .headers(HeadersBuilder.buildHeaders(request.getHeaders()))
                .exchangeToMono(clientResponse ->
                        ClientRestUtil.getBodyResponse(BankEntitiesResponseDTO.class, request, clientResponse,
                                ActionsResponseBuilder.actionsResponseBuilder(ACTION, this.getClass().getName())))
                .timeout(Duration.ofMillis(achProperties.getTimeout()))
                .onErrorMap(TimeoutException.class, ex ->
                        new TechnicalException(ex, TechnicalErrorMessage.TIMEOUT_EXCEPTION))
                .onErrorMap(WebClientException.class, ex ->
                        new TechnicalException(ex, TechnicalErrorMessage.WEB_CLIENT_EXCEPTION))
                .onErrorMap(ex -> ex instanceof TechnicalException || ex instanceof BusinessException ?
                        ex : new TechnicalException(ex, TechnicalErrorMessage.UNEXPECTED_EXCEPTION));
    }
}