package co.com.bancolombia.model.product.gateway.function;

import co.com.bancolombia.model.commons.Context;
import co.com.bancolombia.model.commons.Function;
import co.com.bancolombia.model.product.Product;
import reactor.core.publisher.Mono;

import java.util.Set;


public interface FunctionManager {

    Mono<Set<Function>> enrollProduct(Product product, Context context);
    Mono<Set<Function>> modifyProduct(Product currentProduct, Product newProduct, Context context);
    Mono<Set<Function>> deleteProduct(Product product, Context context);
}