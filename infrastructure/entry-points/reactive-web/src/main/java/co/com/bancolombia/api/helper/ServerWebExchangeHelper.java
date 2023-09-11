package co.com.bancolombia.api.helper;

import co.com.bancolombia.api.logger.LogConstantsEnum;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMessage;
import org.springframework.web.server.ServerWebExchange;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@UtilityClass
public class ServerWebExchangeHelper {

    private static final String EMPTY_STRING = "";
    private static final String REQUEST = "request";
    private static final String RESPONSE = "response";
    private static final String BODY = "body";
    private static final String HEADERS = "headers";
    private static final String TIMESTAMP = "timestamp";


    public static String getTransactionId(ServerWebExchange webExchange) {
        return Optional.ofNullable(webExchange)
                .map(ServerWebExchange::getRequest)
                .map(HttpMessage::getHeaders)
                .map(HttpHeaders::toSingleValueMap)
                .orElse(Collections.emptyMap())
                .getOrDefault(LogConstantsEnum.MESSAGE_ID.getName(), EMPTY_STRING);
    }

    public static String getFirstHeader(ServerWebExchange exchange, String name) {
        return Optional.ofNullable(exchange)
                .map(ServerWebExchange::getRequest)
                .map(HttpMessage::getHeaders)
                .map(headers -> headers.getFirst(name))
                .orElse(EMPTY_STRING);
    }

    public static Map<String, Object> getMessageFromExchange(ServerWebExchange exchange) {
        return Map.of(REQUEST, getRequest(exchange),
                RESPONSE, getResponse(exchange));
    }

    public static Map<String, Object> getRequest(ServerWebExchange exchange) {
        return Map.of(TIMESTAMP, getAttributeFromExchange(exchange, LogConstantsEnum.REQUEST_TIME.getName()),
                HEADERS, getRequestHeaders(exchange),
                BODY, getAttributeFromExchange(exchange, LogConstantsEnum.REQUEST_BODY.getName()));
    }

    public static Object getAttributeFromExchange(ServerWebExchange exchange, String name) {
        return Optional.ofNullable(exchange)
                .map(serverWebExchange -> serverWebExchange.getAttribute(name))
                .orElse(EMPTY_STRING);
    }

    public static Map<String, String> getRequestHeaders(ServerWebExchange exchange) {
        return Optional.ofNullable(exchange)
                .map(ServerWebExchange::getRequest)
                .map(HttpMessage::getHeaders)
                .map(HttpHeaders::toSingleValueMap)
                .orElse(Map.of(EMPTY_STRING, EMPTY_STRING));
    }

    public static Map<String, Object> getResponse(ServerWebExchange exchange) {
        return Map.of(
                TIMESTAMP, TimeStampHelper.getFormattedTimeStamp(System.currentTimeMillis()),
                HEADERS, getResponseHeaders(exchange),
                BODY, getAttributeFromExchange(exchange, LogConstantsEnum.RESPONSE_BODY.getName())
        );
    }

    public static Map<String, String> getResponseHeaders(ServerWebExchange exchange) {
        return Optional.ofNullable(exchange)
                .map(ServerWebExchange::getResponse)
                .map(HttpMessage::getHeaders)
                .map(HttpHeaders::toSingleValueMap)
                .orElse(Map.of(EMPTY_STRING, EMPTY_STRING));
    }
}

