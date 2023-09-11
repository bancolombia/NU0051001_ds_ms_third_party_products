package co.com.bancolombia.rest.product.functions;

import co.com.bancolombia.model.commons.Context;
import co.com.bancolombia.model.exception.BusinessException;
import co.com.bancolombia.model.exception.message.BusinessErrorMessage;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateway.function.FunctionAdapter;
import co.com.bancolombia.rest.commons.ActionsResponseBuilder;
import co.com.bancolombia.rest.commons.ClientRestUtil;
import co.com.bancolombia.rest.commons.HeadersBuilder;
import co.com.bancolombia.rest.commons.Request;
import co.com.bancolombia.rest.config.RestConsumerProperties.AccountManagementProperties;
import co.com.bancolombia.rest.config.RestConsumerProperties.PaymentsProperties;
import co.com.bancolombia.rest.product.functions.dto.ResponseEnrollNequiTransferDTO;
import co.com.bancolombia.rest.product.functions.dto.ResponseEnrollOtherBanksTransferDTO;
import co.com.bancolombia.rest.product.functions.utils.RestFunctionAdapterResponseHandler;
import co.com.bancolombia.rest.product.functions.utils.RestFunctionAdapterUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;
import java.util.Optional;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(PaymentsProperties.class)
public class RestFunctionAdapter implements FunctionAdapter {

    private static final String PAYROLL_PLAN = "Plan Nomina";
    private static final String SUPPLIER_PLAN = "Plan Proveedores";
    private static final String ENROLL_PRODUCT_FOR_PAYROLL_PAYMENT = "EnrollProductForPayrollPayment";
    private static final String ENROLL_PRODUCT_FOR_SUPPLIER_PAYMENT = "EnrollProductForSupplierPayment";
    private static final String ENROLL_PRODUCT_FOR_TRANSFER_OTHER_BANKS = "EnrollProductForTransferOtherBanks";
    private static final String ENROLL_PRODUCT_FOR_TRANSFER_NEQUI_ACCOUNT = "EnrollProductForTransferNequiAccount";
    private static final Pattern SUCCESSFUL_ENROLLMENT_PATTERN = Pattern.compile("^0+$");
    private static final String EMPTY_BODY_RESPONSE = "";
    private final WebClient tokenCredentialsWebClient;
    private final PaymentsProperties paymentsProperties;
    private final AccountManagementProperties accountManagementProperties;
    private final RestFunctionAdapterResponseHandler restFunctionAdapterResponseHandler;


    @Override
    public Mono<Boolean> enrollProductForTransferOtherBanks(Product product, Context context) {
        var request = new Request<>(HeadersBuilder.buildHeaders(context),
                RestFunctionAdapterUtils.buildBodyRequestEnrollOtherBanksTransfer(product, context,
                        getHomologatedProductType(product.getType())),
                URI.create(paymentsProperties.getRegisterBeneficiaryOthersBanksUrl()));
        var actionsResponse = ActionsResponseBuilder
                .actionsResponseBuilder(ENROLL_PRODUCT_FOR_TRANSFER_OTHER_BANKS, this.getClass().getName());

        return tokenCredentialsWebClient.post()
                .uri(request.getUri())
                .headers(HeadersBuilder.buildHeaders(request.getHeaders()))
                .bodyValue(request.getBody())
                .exchangeToMono(clientResponse ->
                        clientResponse.statusCode().is2xxSuccessful() ?
                                ClientRestUtil.getBodyResponse(ResponseEnrollOtherBanksTransferDTO.class, request,
                                                clientResponse, actionsResponse)
                                        .map(response -> SUCCESSFUL_ENROLLMENT_PATTERN.matcher(
                                                response.data().payer().get(0).beneficiary().answerCode()).matches())
                                : restFunctionAdapterResponseHandler.validateResponse(clientResponse, request,
                                actionsResponse, restFunctionAdapterResponseHandler
                                        .getTransferOtherBanksAllowedCodes()))
                .timeout(Duration.ofMillis(paymentsProperties.getTimeout()))
                .onErrorResume(Exception.class, exception -> Mono.just(Boolean.FALSE));
    }

    @Override
    public Mono<Boolean> enrollProductForTransferNequiAccount(Product product, Context context) {
        var request = new Request<>(HeadersBuilder.buildHeaders(context),
                RestFunctionAdapterUtils.buildBodyRequestEnrollNequiTransferDTO(product, context,
                        getHomologatedProductType(product.getType())),
                URI.create(paymentsProperties.getEnrollNequiAccountUrl()));

        return tokenCredentialsWebClient.post()
                .uri(request.getUri())
                .headers(HeadersBuilder.buildHeaders(request.getHeaders()))
                .bodyValue(request.getBody())
                .exchangeToMono(clientResponse -> ClientRestUtil
                        .getBodyResponse(ResponseEnrollNequiTransferDTO.class, request, clientResponse,
                                ActionsResponseBuilder.
                                        actionsResponseBuilder(ENROLL_PRODUCT_FOR_TRANSFER_NEQUI_ACCOUNT,
                                                this.getClass().getName()))
                        .map(response -> SUCCESSFUL_ENROLLMENT_PATTERN.matcher(
                                response.data().beneficiary().account().answerCode()).matches()))
                .timeout(Duration.ofMillis(paymentsProperties.getTimeout()))
                .onErrorResume(Exception.class, exception -> Mono.just(Boolean.FALSE));
    }

    @Override
    public Mono<Boolean> enrollProductForPayrollPayment(Product product, Context context) {
        return enrollSupplierOrPayrollPayment(product, context, PAYROLL_PLAN,
                ENROLL_PRODUCT_FOR_PAYROLL_PAYMENT);
    }

    @Override
    public Mono<Boolean> enrollProductForSupplierPayment(Product product, Context context) {
        return enrollSupplierOrPayrollPayment(product, context, SUPPLIER_PLAN,
                ENROLL_PRODUCT_FOR_SUPPLIER_PAYMENT);
    }

    public Mono<Boolean> enrollSupplierOrPayrollPayment(Product product, Context context, String plain,
                                                        String action) {
        var request = new Request<>(HeadersBuilder.buildHeaders(context),
                RestFunctionAdapterUtils.buildBodyRequestEnrollPayrollAndSupplier(product, context, plain,
                        getHomologatedProductType(product.getType())),
                URI.create(paymentsProperties.getPayrollAndSupplierPaymentUrl()));
        var actionsResponse = ActionsResponseBuilder.actionsResponseBuilder(action,
                this.getClass().getName());

        return tokenCredentialsWebClient.post()
                .uri(request.getUri())
                .headers(HeadersBuilder.buildHeaders(request.getHeaders()))
                .bodyValue(request.getBody())
                .exchangeToMono(clientResponse ->
                        Mono.just(clientResponse.statusCode().value() == HttpStatus.NO_CONTENT.value())
                                .filter(Boolean.TRUE::equals)
                                .flatMap(success -> {
                                    ClientRestUtil.buildLogMessage(request, clientResponse.headers().asHttpHeaders()
                                            .toSingleValueMap(), EMPTY_BODY_RESPONSE, ActionsResponseBuilder
                                            .actionsResponseBuilder(action, this.getClass().getName()));
                                    return Mono.just(Boolean.TRUE);
                                })
                                .switchIfEmpty(restFunctionAdapterResponseHandler.validateResponse(clientResponse,
                                        request, actionsResponse,
                                        restFunctionAdapterResponseHandler.getSupplierOrPayrollAllowedCodes()))
                )
                .timeout(Duration.ofMillis(paymentsProperties.getTimeout()))
                .onErrorResume(Exception.class, exception -> Mono.just(Boolean.FALSE));
    }

    @Override
    public Mono<Boolean> deleteProductForPayrollPayment(Product product, Context context) {
        return Mono.just(Boolean.TRUE);
    }

    @Override
    public Mono<Boolean> deleteProductForSupplierPayment(Product product, Context context) {
        return Mono.just(Boolean.TRUE);
    }

    @Override
    public Mono<Boolean> deleteProductForTransferNequiAccount(Product product, Context context) {
        return Mono.just(Boolean.TRUE);
    }

    @Override
    public Mono<Boolean> deleteProductForTransferOtherBanks(Product product, Context context) {
        return Mono.just(Boolean.TRUE);
    }

    private String getHomologatedProductType(String productType) {
        return Optional.ofNullable(accountManagementProperties.getHomologationCodes().get(productType))
                .orElseThrow((() -> new BusinessException(BusinessErrorMessage.UNAVAILABLE_PRODUCT_TYPE)));
    }

}