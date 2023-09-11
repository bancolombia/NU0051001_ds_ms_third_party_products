package co.com.bancolombia.api.validations;

import co.com.bancolombia.api.properties.RouteProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class ProductValidationRouter {

    private final RouteProperties routeProperties;

    @Bean
    public RouterFunction<ServerResponse> productValidationRouterFunction(
            ProductValidationHandler productValidationHandler) {
        return route().GET(routeProperties.getProduct().getValidateOwnership(),
                productValidationHandler::validateOwnership).build();
    }
}