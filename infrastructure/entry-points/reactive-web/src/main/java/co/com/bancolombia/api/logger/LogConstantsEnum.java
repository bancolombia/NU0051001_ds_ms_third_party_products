package co.com.bancolombia.api.logger;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LogConstantsEnum {

    SERVICE_NAME("ds_ms_third_party_products"),
    TIME_PATTERN("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"),
    RESPONSE_BODY("RESPONSE_BODY"),
    REQUEST_TIME("REQUEST_TIME"),
    REQUEST_BODY("REQUEST_BODY"),
    MESSAGE_ID("message-id");

    private final String name;
}