package co.com.bancolombia.api.logger;

import co.com.bancolombia.api.helper.TimeStampHelper;
import co.com.bancolombia.logging.technical.LoggerFactory;
import co.com.bancolombia.logging.technical.logger.TechLogger;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

@Component
public class InfoLogFilter implements WebFilter {

    private static final TechLogger techLogger = LoggerFactory.getLog(LogConstantsEnum.SERVICE_NAME.getName());
    private static final Pattern LINE_BREAK_REGEX = Pattern.compile("(\\r\\n|\\n|\\r)");
    private static final String EMPTY_STRING = "";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        exchange.getAttributes().put(LogConstantsEnum.REQUEST_TIME.getName(),
                TimeStampHelper.getFormattedTimeStamp(System.currentTimeMillis()));

        return chain.filter(getModifiedServerWebExchange(exchange))
                .doAfterTerminate(() -> techLogger.info(TechMessage.getTechInfoMessage(exchange)));
    }


    private ServerWebExchange getModifiedServerWebExchange(ServerWebExchange exchange) {
        return exchange.mutate()
                .request(getServerHttpRequestDecorator(exchange))
                .response(getServerHttpResponseDecorator(exchange))
                .build();
    }

    private ServerHttpRequestDecorator getServerHttpRequestDecorator(ServerWebExchange exchange) {
        return new ServerHttpRequestDecorator(exchange.getRequest()) {

            @Override
            public Flux<DataBuffer> getBody() {
                return super.getBody().doOnNext(dataBuffer -> {
                    var charBuffer = StandardCharsets.UTF_8.decode(dataBuffer.toByteBuffer());
                    exchange.getAttributes().put(LogConstantsEnum.REQUEST_BODY.getName(),
                            LINE_BREAK_REGEX.matcher(charBuffer.toString()).replaceAll(EMPTY_STRING));
                });
            }
        };
    }

    private ServerHttpResponseDecorator getServerHttpResponseDecorator(ServerWebExchange exchange) {
        return new ServerHttpResponseDecorator(exchange.getResponse()) {

            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                final Flux<DataBuffer> dataBufferFlux = Flux.from(body)
                        .map(dataBuffer -> {
                            var charBuffer = StandardCharsets.UTF_8.decode(dataBuffer.toByteBuffer());
                            exchange.getAttributes().put(LogConstantsEnum.RESPONSE_BODY.getName(),
                                    charBuffer.toString());
                            return dataBuffer;
                        });
                return super.writeWith(dataBufferFlux);
            }
        };
    }
}