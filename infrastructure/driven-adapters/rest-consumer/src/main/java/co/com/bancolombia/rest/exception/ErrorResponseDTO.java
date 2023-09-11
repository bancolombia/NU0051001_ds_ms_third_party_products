package co.com.bancolombia.rest.exception;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponseDTO {

    private List<ErrorDTO> errors;

    @Getter
    @EqualsAndHashCode
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ErrorDTO {

        private String reason;
        private String domain;
        private String code;
        @JsonAlias("detail")
        private String message;
    }
}