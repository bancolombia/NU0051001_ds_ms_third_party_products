package co.com.bancolombia.api.commons;

import co.com.bancolombia.api.commons.validation.ConstraintValidator;
import co.com.bancolombia.model.commons.Context;

import co.com.bancolombia.model.user.Customer;
import co.com.bancolombia.model.user.Identification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ContextBuilder {

    private final ConstraintValidator constraintValidator;

    public Mono<Context> getContext(ServerRequest serverRequest) {
        return Mono.just(ContextDTO.builder()
                        .id(serverRequest.headers().firstHeader("message-id"))
                        .sessionId(serverRequest.headers().firstHeader("session-tracker"))
                        .channel(serverRequest.headers().firstHeader("channel"))
                        .requestDate(serverRequest.headers().firstHeader("request-timestamp"))
                        .domain(serverRequest.path())
                        .agent(ContextDTO.Agent.builder()
                                .name(serverRequest.headers().firstHeader("user-agent"))
                                .appVersion(serverRequest.headers().firstHeader("app-version")).build())
                        .device(ContextDTO.Device.builder()
                                .id(serverRequest.headers().firstHeader("device-id"))
                                .type(serverRequest.headers().firstHeader("platform-type"))
                                .ip(serverRequest.headers().firstHeader("ip")).build())
                        .contentType(serverRequest.headers().firstHeader("content-type"))
                        .authorization(serverRequest.headers().firstHeader("authorization"))
                        .customer(ContextDTO.Customer.builder()
                                .identification(ContextDTO.Customer.Identification.builder()
                                        .type(serverRequest.headers().firstHeader("customer-identification-type"))
                                        .number(serverRequest.headers().firstHeader("customer-identification-number"))
                                        .build())
                                .mdmCode(serverRequest.headers().firstHeader("mdm-code"))
                                .build())
                        .build())
                .flatMap(constraintValidator::validateData)
                .map(ContextBuilder::toContext);
    }

    public static Context toContext(ContextDTO contextDTO) {
        return Context.builder()
                .id(contextDTO.getId())
                .sessionId(contextDTO.getSessionId())
                .channel(contextDTO.getChannel())
                .requestDate(contextDTO.getRequestDate())
                .domain(contextDTO.getDomain())
                .agent(Context.Agent.builder()
                        .appVersion(contextDTO.getAgent().getAppVersion())
                        .name(contextDTO.getAgent().getName()).build())
                .customer(new Customer(new Identification(contextDTO.getCustomer().getIdentification().getType(),
                        contextDTO.getCustomer().getIdentification().getNumber()),
                        contextDTO.getCustomer().getMdmCode()))
                .device(Context.Device.builder()
                        .ip(contextDTO.getDevice().getIp())
                        .id(contextDTO.getDevice().getId())
                        .type(contextDTO.getDevice().getType()).build())
                .contentType(contextDTO.getContentType())
                .authorization(contextDTO.getAuthorization())
                .build();
    }
}
