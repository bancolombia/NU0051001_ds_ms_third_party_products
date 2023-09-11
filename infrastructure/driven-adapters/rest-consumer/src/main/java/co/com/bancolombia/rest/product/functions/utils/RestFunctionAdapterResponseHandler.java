package co.com.bancolombia.rest.product.functions.utils;

import co.com.bancolombia.rest.commons.ClientRestUtil;
import co.com.bancolombia.rest.commons.Request;
import co.com.bancolombia.rest.exception.ErrorResponseDTO;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
@Getter
public class RestFunctionAdapterResponseHandler {

    private final Set<String> supplierOrPayrollAllowedCodes = new HashSet<>();
    private final Set<String> transferOtherBanksAllowedCodes = new HashSet<>();

    public RestFunctionAdapterResponseHandler() {

        /* ACHFulfillment Transfers - Account pending confirmation */
        transferOtherBanksAllowedCodes.add("BP022");


        /* Payroll And Supplier Payment-Beneficiaries - Beneficiary already enrolled with the payer */
        supplierOrPayrollAllowedCodes.add("BPR002");

    }

    public Mono<Boolean> validateResponse(ClientResponse clientResponse, Request<?> request,
                                          Map<String, String> actionsResponse, Set<String> allowedCodes) {

        return getErrorCodeFromResponse(clientResponse, request, actionsResponse)
                .flatMap(code -> Mono.just(allowedCodes.contains(code)))
                .switchIfEmpty(Mono.just(Boolean.FALSE));
    }

    private Mono<String> getErrorCodeFromResponse(ClientResponse clientResponse, Request<?> request,
                                                  Map<String, String> actionsResponse) {

        Map<String, String> responseHeaders = clientResponse.headers().asHttpHeaders().toSingleValueMap();
        return clientResponse.bodyToMono(ErrorResponseDTO.class)
                .switchIfEmpty(Mono.defer(() ->
                        Mono.error(ClientRestUtil.buildTechnicalEmptyBodyException(clientResponse))))
                .flatMap(errorResponse -> {
                    ClientRestUtil.buildLogMessage(request, responseHeaders, errorResponse, actionsResponse);
                    return Mono.just(errorResponse)
                            .map(errorResponseDTO -> errorResponseDTO.getErrors().get(0).getCode());
                });

    }
}
