package co.com.bancolombia.api.properties;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties({RouteProperties.ProductProperties.class, RouteProperties.ParameterProperties.class})
public class RouteProperties {

    private final ProductProperties product;
    private final ParameterProperties parameter;

    @Getter
    @RequiredArgsConstructor
    @ConfigurationProperties(prefix = "routes.path-mapping.product")
    public static class ProductProperties {

        @NotBlank
        private final String enroll;

        @NotBlank
        private final String validateOwnership;

        @NotBlank
        private final String paginatedSearch;

        @NotBlank
        private final String modify;

        @NotBlank
        private final String delete;

        @NotBlank
        private final String download;
    }

    @Getter
    @RequiredArgsConstructor
    @ConfigurationProperties(prefix = "routes.path-mapping.parameter")
    public static class ParameterProperties {

        @NotBlank
        private final String allParameters;
    }
}
