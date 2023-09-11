package co.com.bancolombia.rest.account.customer;

import co.com.bancolombia.exception.technical.TechnicalException;
import co.com.bancolombia.exception.technical.message.TechnicalErrorMessage;
import co.com.bancolombia.model.commons.Context;
import co.com.bancolombia.model.exception.BusinessException;
import co.com.bancolombia.model.user.Identification;
import co.com.bancolombia.model.user.gateway.CustomerAdapter;
import co.com.bancolombia.rest.account.customer.dto.CustomerBasicInformationRequestDTO;
import co.com.bancolombia.rest.account.customer.dto.CustomerBasicInformationRequestDTO.CustomerBasicInformationRequest;
import co.com.bancolombia.rest.account.customer.dto.CustomerBasicInformationResponseDTO;
import co.com.bancolombia.rest.commons.ActionsResponseBuilder;
import co.com.bancolombia.rest.commons.ClientRestUtil;
import co.com.bancolombia.rest.commons.HeadersBuilder;
import co.com.bancolombia.rest.commons.Request;
import co.com.bancolombia.rest.config.RestConsumerProperties.CustomerProperties;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;

import java.net.URI;

@Configuration
public class RestCustomerAdapter implements CustomerAdapter {

    private final WebClient webClient;
    private final CustomerProperties customerProperties;
    private static final String ACTION = "BasicInformation";

    public RestCustomerAdapter(@Qualifier("apicWebClient") WebClient webClient,
                               CustomerProperties accountManagementProperties) {
        this.webClient = webClient;
        this.customerProperties = accountManagementProperties;
    }

    @Override
    public Mono<String> getCustomerName(Identification identification, Context context) {

        var request = new Request<>(HeadersBuilder.buildHeaders(context),
                buildBody(identification),
                URI.create(customerProperties.getBasicInformationUrl()));

        return webClient.post()
                .uri(request.getUri())
                .headers(HeadersBuilder.buildHeaders(request.getHeaders()))
                .bodyValue(request.getBody())
                .exchangeToMono(clientResponse -> ClientRestUtil
                        .getBodyResponse(CustomerBasicInformationResponseDTO.class,
                                request, clientResponse, ActionsResponseBuilder
                                        .actionsResponseBuilder(ACTION, this.getClass().getName())))
                .timeout(Duration.ofMillis(customerProperties.getTimeout()))
                .map(customerBasicInformationResponse -> customerBasicInformationResponse.getData().getFullName())
                .onErrorMap(TimeoutException.class, ex ->
                        new TechnicalException(ex, TechnicalErrorMessage.TIMEOUT_EXCEPTION))
                .onErrorMap(WebClientException.class, ex ->
                        new TechnicalException(ex, TechnicalErrorMessage.WEB_CLIENT_EXCEPTION))
                .onErrorMap(ex -> (ex instanceof TechnicalException || ex instanceof BusinessException) ?
                        ex : new TechnicalException(ex, TechnicalErrorMessage.UNEXPECTED_EXCEPTION));
    }

    private CustomerBasicInformationRequestDTO buildBody(Identification identification) {
        return new CustomerBasicInformationRequestDTO(
                new CustomerBasicInformationRequest(identification.type(), identification.number()));
    }
}
