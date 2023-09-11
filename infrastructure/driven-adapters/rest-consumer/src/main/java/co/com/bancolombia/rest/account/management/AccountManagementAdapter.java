package co.com.bancolombia.rest.account.management;

import co.com.bancolombia.IdentificationHomologator;
import co.com.bancolombia.exception.technical.TechnicalException;
import co.com.bancolombia.exception.technical.message.TechnicalErrorMessage;
import co.com.bancolombia.model.commons.Context;
import co.com.bancolombia.model.exception.BusinessException;
import co.com.bancolombia.model.exception.message.BusinessErrorMessage;
import co.com.bancolombia.rest.account.management.dto.AccountOwnerShipValidationRequestDTO;
import co.com.bancolombia.rest.account.management.dto.AccountOwnerShipValidationResponseDTO;
import co.com.bancolombia.rest.commons.ActionsResponseBuilder;
import co.com.bancolombia.rest.commons.ClientRestUtil;
import co.com.bancolombia.rest.commons.HeadersBuilder;
import co.com.bancolombia.rest.commons.Request;
import co.com.bancolombia.rest.commons.dto.CustomerDTO;
import co.com.bancolombia.rest.commons.dto.IdentificationDTO;
import co.com.bancolombia.rest.config.RestConsumerProperties.AccountManagementProperties;
import co.com.bancolombia.rest.account.management.dto.AccountOwnerShipValidationRequestDTO.AccountOwnerShipValidationRequest;
import co.com.bancolombia.model.product.gateway.ProductAccountValidationAdapter;
import co.com.bancolombia.model.user.Identification;
import co.com.bancolombia.rest.account.management.dto.AccountDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

@Component
public class AccountManagementAdapter implements ProductAccountValidationAdapter {

    private final WebClient webClient;
    private final AccountManagementProperties accountManagementProperties;
    private final IdentificationHomologator identificationHomologator;
    private static final String ACTION = "ValidateOwnerShip";
    private static final String DEFAULT_NOT_RELATION_MESSAGE = "CLIENTE NO TIENE RELACIÓN CON LA CUENTA";
    private final Map<String, String> homolagatedAccounts;

    public AccountManagementAdapter(@Qualifier("apicWebClient") WebClient webClient,
                                    AccountManagementProperties accountManagementProperties,
                                    IdentificationHomologator identificationHomologator) {
        this.webClient = webClient;
        this.accountManagementProperties = accountManagementProperties;
        this.identificationHomologator = identificationHomologator;
        this.homolagatedAccounts = accountManagementProperties.getHomologationCodes();
    }

    @Override
    public Mono<Boolean> validateOwnership(String productNumber, String productType,
                                           Identification identification, Context context) {
        return identificationHomologator.getHomologatedIdentificationType(identification)
                .flatMap(identificationType -> {
                    var request = new Request<>(HeadersBuilder.buildHeaders(context),
                            buildBody(getHomologatedProductType(productType), productNumber,
                                    identificationType, identification.number()),
                            URI.create(accountManagementProperties.getValidationAccountUrl()));
                    return webClient.post()
                            .uri(request.getUri())
                            .headers(HeadersBuilder.buildHeaders(request.getHeaders()))
                            .bodyValue(request.getBody())
                            .exchangeToMono(clientResponse ->
                                    ClientRestUtil.getBodyResponse(AccountOwnerShipValidationResponseDTO.class, request,
                                                    clientResponse, ActionsResponseBuilder
                                                            .actionsResponseBuilder(ACTION, this.getClass().getName()))
                                            .map(response -> !response.getData().get(0).getParticipant()
                                                    .getRelation().equals(DEFAULT_NOT_RELATION_MESSAGE)))
                            .timeout(Duration.ofMillis(accountManagementProperties.getTimeout()))
                            .onErrorMap(TimeoutException.class, ex ->
                                    new TechnicalException(ex, TechnicalErrorMessage.TIMEOUT_EXCEPTION))
                            .onErrorMap(WebClientException.class, ex ->
                                    new TechnicalException(ex, TechnicalErrorMessage.WEB_CLIENT_EXCEPTION))
                            .onErrorMap(ex -> ex instanceof TechnicalException || ex instanceof BusinessException ?
                                    ex : new TechnicalException(ex, TechnicalErrorMessage.UNEXPECTED_EXCEPTION));
                });
    }

    private String getHomologatedProductType(String productType) {
        return Optional.ofNullable(homolagatedAccounts.get(productType))
                .orElseThrow((() -> new BusinessException(BusinessErrorMessage.UNAVAILABLE_PRODUCT_TYPE)));
    }

    private static AccountOwnerShipValidationRequestDTO buildBody(String productType, String productNumber,
                                                                  String identificationType,
                                                                  String identificationNumber) {
        return new AccountOwnerShipValidationRequestDTO(
                List.of(new AccountOwnerShipValidationRequest(
                        new CustomerDTO(new IdentificationDTO(identificationType, identificationNumber)),
                        new AccountDTO(productType, productNumber)))
        );
    }
}
