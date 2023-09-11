package co.com.bancolombia.api.commons;

import co.com.bancolombia.api.commons.validation.ConstraintValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ParameterBuilder {
    private final ConstraintValidator constraintValidator;

    public Mono<ParameterRequestDTO> getParameterRequestDTO(ServerRequest serverRequest) {
        return Mono.just(ParameterRequestDTO.builder()
                        .id(serverRequest.headers().firstHeader("message-id"))
                        .sessionId(serverRequest.headers().firstHeader("session-tracker"))
                        .channel(serverRequest.headers().firstHeader("channel"))
                        .requestDate(serverRequest.headers().firstHeader("request-timestamp"))
                        .domain(serverRequest.path())
                        .agent(BaseContextDTO.Agent.builder()
                                .name(serverRequest.headers().firstHeader("user-agent"))
                                .appVersion(serverRequest.headers().firstHeader("app-version")).build())
                        .device(BaseContextDTO.Device.builder()
                                .id(serverRequest.headers().firstHeader("device-id"))
                                .type(serverRequest.headers().firstHeader("platform-type"))
                                .ip(serverRequest.headers().firstHeader("ip")).build())
                        .contentType(serverRequest.headers().firstHeader("content-type"))
                        .authorization(serverRequest.headers().firstHeader("authorization"))
                        .build())
                .flatMap(constraintValidator::validateData);
    }
}
