package co.com.bancolombia.api.parameter;

import co.com.bancolombia.api.builders.RequestBuilder;
import co.com.bancolombia.api.commons.ContextBuilder;
import co.com.bancolombia.api.commons.ParameterBuilder;
import co.com.bancolombia.api.commons.ParameterRequestDTO;
import co.com.bancolombia.api.commons.validation.ConstraintValidator;
import co.com.bancolombia.api.exception.ExceptionHandler;
import co.com.bancolombia.api.parameter.dto.ParameterDataResponseDTO;
import co.com.bancolombia.api.properties.RouteProperties;
import co.com.bancolombia.builders.ContextCreator;
import co.com.bancolombia.model.commons.Context;
import co.com.bancolombia.model.parameters.ParameterAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(properties = {
        "routes.path-mapping.parameter.allParameters=/parameters",
        "adapter.rest-consumer.channel-management.transactionCode=100",
        "adapter.rest-consumer.channel-management.parameterName=inscripcionProductosTerceros",
})
@ContextConfiguration(classes = {
        RouteProperties.class,
        ParameterRouter.class,
        ConstraintValidator.class,
        ContextBuilder.class,
        ParameterHandler.class,
        ExceptionHandler.class
})
class ParameterRouterTest {

    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private ParameterAdapter parameterAdapter;
    @MockBean
    private ParameterBuilder parameterBuilder;

    private static final String PARAMETER_ROUTE = "/parameters";

    private Context context = ContextCreator.buildNewContext();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldGetParametersSuccessfully() {
        var parameterId = "parameterId";
        var parameterName = "parameterName";

        Map<String, String> responseParameters = new HashMap<>();

        var expectProductDataResponseDTO = new ParameterDataResponseDTO(
                new ParameterDataResponseDTO.ParametersDTO(
                        List.of(Map.of("code", parameterId, "name", parameterName)),
                        List.of(Map.of("code", parameterId, "name", parameterName))));


        ParameterRequestDTO parameterRequestDTO =
                ParameterRequestDTO.builder().id(context.getId()).channel(context.getChannel()).build();

        responseParameters.put(parameterId, parameterName);

        when(parameterBuilder.getParameterRequestDTO(any())).thenReturn(Mono.just(parameterRequestDTO));
        when(parameterAdapter.getProductTypes(context.getChannel(),context.getId())).thenReturn(Mono.just(responseParameters));
        when(parameterAdapter.getDocumentTypes(context.getChannel(),context.getId())).thenReturn(Mono.just(responseParameters));

        webTestClient.get()
                .uri(PARAMETER_ROUTE)
                .headers(RequestBuilder.getContextHeaders)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK)
                .expectBody(ParameterDataResponseDTO.class)
                .isEqualTo(expectProductDataResponseDTO);
    }

    @Test
    void shouldThrowExceptionWhenWhenContextHeadersDoesNotExist() {
        webTestClient.get()
                .uri(PARAMETER_ROUTE)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody()
                .jsonPath("$.errors").isNotEmpty()
                .jsonPath("$.errors[0].reason").isNotEmpty();
    }
}