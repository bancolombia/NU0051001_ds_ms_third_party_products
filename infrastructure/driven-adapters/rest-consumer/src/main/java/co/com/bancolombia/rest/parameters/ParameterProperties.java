package co.com.bancolombia.rest.parameters;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "adapter.rest-consumer.channel-management")
public class ParameterProperties {

    @NotBlank private final String parameterPath;
    @NotBlank private final String documentTypesPath;
    @NotBlank private final String transactionCode;
    @NotBlank private final String parameterName;
    @NotBlank private final Long timeout;

}