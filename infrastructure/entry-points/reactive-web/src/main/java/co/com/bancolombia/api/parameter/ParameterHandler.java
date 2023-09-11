package co.com.bancolombia.api.parameter;

import co.com.bancolombia.api.commons.ParameterBuilder;
import co.com.bancolombia.api.parameter.mapper.ParameterResponseMapper;
import co.com.bancolombia.model.parameters.ParameterAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.function.TupleUtils;

@Component
@RequiredArgsConstructor
public class ParameterHandler {

    private final ParameterAdapter parameterAdapter;
    private final ParameterBuilder parameterBuilder;

    public Mono<ServerResponse> getParameters(ServerRequest serverRequest) {
        return parameterBuilder.getParameterRequestDTO(serverRequest)
                .flatMap(parameterRequestDTO -> Mono.zip(
                        parameterAdapter.getProductTypes(parameterRequestDTO.getChannel(),
                                parameterRequestDTO.getId()),
                        parameterAdapter.getDocumentTypes(parameterRequestDTO.getChannel(),
                                parameterRequestDTO.getId())))
                .map(TupleUtils.function(ParameterResponseMapper::mapToParametersResponse))
                .flatMap(response -> ServerResponse
                        .status(HttpStatus.OK)
                        .bodyValue(response));
    }
}