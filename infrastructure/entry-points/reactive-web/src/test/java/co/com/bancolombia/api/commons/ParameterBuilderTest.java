package co.com.bancolombia.api.commons;

import co.com.bancolombia.api.commons.validation.ConstraintValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class ParameterBuilderTest {

    public static final String VALUE = "123";
    public static final String TEST_CHANNEL = "test-channel";
    public static final String MESSAGE_ID = "message-id";
    public static final String CHANNEL = "channel";
    private ParameterBuilder parameterBuilder;
    private ConstraintValidator constraintValidator;


    @BeforeEach
    void setUp() {
        constraintValidator = mock(ConstraintValidator.class);
        parameterBuilder = new ParameterBuilder(constraintValidator);
    }

    @Test
    void getParameterRequestDTOSuccessfully() {
        ServerRequest serverRequest = mock(ServerRequest.class);
        when(serverRequest.headers()).thenReturn(mock(ServerRequest.Headers.class));
        when(serverRequest.headers().firstHeader(MESSAGE_ID)).thenReturn(VALUE);
        when(serverRequest.headers().firstHeader(CHANNEL)).thenReturn(TEST_CHANNEL);

        ParameterRequestDTO expectedDTO = ParameterRequestDTO.builder()
                .id(VALUE)
                .channel(TEST_CHANNEL)
                .build();

        when(constraintValidator.validateData(any())).thenReturn(Mono.just(expectedDTO));

        Mono<ParameterRequestDTO> resultMono = parameterBuilder.getParameterRequestDTO(serverRequest);

        StepVerifier.create(resultMono)
                .expectNext(expectedDTO)
                .verifyComplete();
    }
}