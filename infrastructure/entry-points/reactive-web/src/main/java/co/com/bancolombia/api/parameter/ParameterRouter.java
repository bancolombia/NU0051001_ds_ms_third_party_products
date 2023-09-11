package co.com.bancolombia.api.parameter;

import co.com.bancolombia.api.properties.RouteProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class ParameterRouter {

    private final RouteProperties routeProperties;

    @Bean
    public RouterFunction<ServerResponse> parameterRoutes(ParameterHandler parameterHandler) {
        return route().GET(routeProperties.getParameter().getAllParameters(), parameterHandler::getParameters).build();
    }
}