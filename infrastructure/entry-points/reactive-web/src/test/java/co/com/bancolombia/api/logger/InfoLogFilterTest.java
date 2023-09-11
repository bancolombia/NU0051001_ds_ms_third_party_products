package co.com.bancolombia.api.logger;

import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpMethod;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class InfoLogFilterTest {

    @InjectMocks
    private InfoLogFilter infoLogFilter;

    @Mock
    private WebFilterChain filterChain;

    private final ArgumentCaptor<ServerWebExchange> captor = ArgumentCaptor.forClass(ServerWebExchange.class);
    private final LogCaptor logCaptor = LogCaptor.forRoot();

    @BeforeEach
    void setUp() {
        when(filterChain.filter(captor.capture())).thenReturn(Mono.empty());
    }

    @Test
    void shouldWriteInfoLog() {

        String uri = "URI";
        String appName = "ds_ms_third_party_products";

        String expectedMessage = "ObjectTechMsg(appName=ds_ms_third_party_products, transactionId=message-id, actionName="+ uri + ", "+
                "serviceName=ds_ms_third_party_products, componentName=InfoLogFilter, tagList=[, app-version ], message=";

        MockServerHttpRequest request = MockServerHttpRequest
                .method(HttpMethod.GET, uri)
                .header(LogConstantsEnum.MESSAGE_ID.getName(), LogConstantsEnum.MESSAGE_ID.getName())
                .build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        infoLogFilter.filter(exchange, filterChain).subscribe();

        String capturedOutput = logCaptor.getInfoLogs().get(0);

        assertTrue(capturedOutput.contains(expectedMessage));
    }

    @Test
    void shouldRunMethodFilterWithoutErrors() {
        MockServerHttpRequest request = MockServerHttpRequest
                .method(HttpMethod.GET, anyString())
                .header(LogConstantsEnum.MESSAGE_ID.getName(), LogConstantsEnum.MESSAGE_ID.getName())
                .build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        infoLogFilter.filter(exchange, filterChain)
                .as(StepVerifier::create)
                .verifyComplete();
    }
}
