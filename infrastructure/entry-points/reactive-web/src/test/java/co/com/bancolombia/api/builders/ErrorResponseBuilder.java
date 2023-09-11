package co.com.bancolombia.api.builders;

import co.com.bancolombia.api.exception.dto.ErrorResponse;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class ErrorResponseBuilder {

    public ErrorResponse buildErrorResponse(String reason, String domain, String code, String message) {
        return new ErrorResponse(List.of(ErrorResponse.ErrorDescription.builder()
                .reason(reason)
                .domain(domain)
                .code(code)
                .message(message).build()));
    }
}