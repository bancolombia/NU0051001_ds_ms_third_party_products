package co.com.bancolombia.rest.commons;

import lombok.Getter;
import lombok.NonNull;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class Request<T> {

    private final Map<String, String> headers;
    private final String timestamp;
    private final URI uri;
    private final T body;

    public Request(@NonNull Map<String, String> headers, @NonNull T body, URI uri) {
        this.headers = headers;
        this.body = body;
        this.timestamp = LocalDateTime.now().toString();
        this.uri = uri;
    }
}