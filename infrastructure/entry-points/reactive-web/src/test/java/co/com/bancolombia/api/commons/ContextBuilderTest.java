package co.com.bancolombia.api.commons;

import co.com.bancolombia.api.builders.ContextTestBuilder;
import co.com.bancolombia.api.commons.validation.ConstraintValidator;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.MockitoAnnotations.openMocks;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;


class ContextBuilderTest {


    private ContextBuilder contextBuilder;

    @Mock
    private ConstraintValidator constraintValidator;

    @Mock
    private Validator validator;

    @BeforeEach
    void setUp() {
        openMocks(this);
        constraintValidator = new ConstraintValidator(validator);
        contextBuilder = new ContextBuilder(constraintValidator);
    }

    @Test
    void shouldGetContextDTOSuccessfully() {

        MockServerRequest serverRequest = MockServerRequest.builder()
                .header("message-id", ContextTestBuilder.ID)
                .header("mdm-code", ContextTestBuilder.MDM_CODE)
                .header("session-tracker", ContextTestBuilder.SESSION_ID)
                .header("channel", ContextTestBuilder.CHANNEL)
                .header("domain", ContextTestBuilder.DOMAIN)
                .header("request-timestamp", ContextTestBuilder.REQUEST_DATE)
                .header("user-agent", ContextTestBuilder.AGENT_NAME)
                .header("app-version", ContextTestBuilder.AGENT_APP_VERSION)
                .header("device-id", ContextTestBuilder.DEVICE_ID)
                .header("platform-type", ContextTestBuilder.DEVICE_TYPE)
                .header("ip", ContextTestBuilder.DEVICE_IP)
                .header("content-type", ContextTestBuilder.CONTENT_TYPE)
                .header("authorization", ContextTestBuilder.AUTHORIZATION)
                .header("customer-identification-type", ContextTestBuilder.IDENTIFICATION_TYPE)
                .header("customer-identification-number", ContextTestBuilder.IDENTIFICATION_NUMBER)
                .build();

        var contexto = contextBuilder.getContext(serverRequest);
        assertEquals(serverRequest.headers().firstHeader("channel"), contexto.block().getChannel());

    }

}
