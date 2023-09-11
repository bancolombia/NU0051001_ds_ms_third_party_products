package co.com.bancolombia.api.product;

import co.com.bancolombia.api.properties.RouteProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class ProductManagementRouter {

    private final RouteProperties routeProperties;

    @Bean
    public RouterFunction<ServerResponse> productManagementRouterFunction(
            ProductManagementHandler productManagementHandler) {
        return route()
                .POST(routeProperties.getProduct().getEnroll(), productManagementHandler::enrollProduct)
                .POST(routeProperties.getProduct().getPaginatedSearch(),
                        productManagementHandler::searchPaginatedProducts)
                .POST(routeProperties.getProduct().getDownload(),
                        productManagementHandler::downloadActiveProducts)
                .PATCH(routeProperties.getProduct().getModify(), productManagementHandler::modifyProduct)
                .DELETE(routeProperties.getProduct().getDelete(), productManagementHandler::deleteProduct)
                .build();
    }
}
