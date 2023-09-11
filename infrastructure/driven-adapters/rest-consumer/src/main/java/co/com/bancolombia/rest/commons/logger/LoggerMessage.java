package co.com.bancolombia.rest.commons.logger;

import co.com.bancolombia.rest.commons.Request;
import co.com.bancolombia.rest.commons.Response;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.RequiredArgsConstructor;
import lombok.ToString;


@ToString
@RequiredArgsConstructor
public class LoggerMessage{

    @JsonSerialize
    private final Request<?> request;
    @JsonSerialize
    private final Response<?> response;
}

