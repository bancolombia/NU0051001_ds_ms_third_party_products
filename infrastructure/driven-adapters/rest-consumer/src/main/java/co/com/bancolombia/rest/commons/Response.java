package co.com.bancolombia.rest.commons;

import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class Response<T> {

    private final Map<String, String> headers;
    private final String timestamp;
    private final T body;

    public Response(@NonNull Map<String, String> headers, @NonNull T body) {
        this.headers = headers;
        this.body = body;
        this.timestamp = LocalDateTime.now().toString();
    }
}