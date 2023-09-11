package co.com.bancolombia.rest.commons;

import co.com.bancolombia.model.commons.Context;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;

import java.util.Map;
import java.util.HashMap;
import java.util.function.Consumer;

@UtilityClass
public class HeadersBuilder {

    public static final String MESSAGE_ID = "message-id";
    public static final String CHANNEL = "channel";

    public static Consumer<HttpHeaders> buildHeaders(Map<String, String> headersMap) {
        return httpHeaders -> headersMap.forEach(httpHeaders::add);
    }

    public Map<String, String> buildHeaders(Context context) {
        Map<String, String> plainHeaders = new HashMap<>();
        plainHeaders.put(MESSAGE_ID, context.getId());
        plainHeaders.put(CHANNEL, context.getChannel());
        return plainHeaders;
    }

    public Map<String, String> buildHeaders(String channel, String messageId) {
        Map<String, String> plainHeaders = new HashMap<>();
        plainHeaders.put(MESSAGE_ID, messageId);
        plainHeaders.put(CHANNEL, channel);
        return plainHeaders;
    }
}
