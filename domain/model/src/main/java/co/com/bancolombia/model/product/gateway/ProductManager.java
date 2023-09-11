package co.com.bancolombia.model.product.gateway;

import co.com.bancolombia.model.commons.Context;
import co.com.bancolombia.model.product.Product;
import reactor.core.publisher.Mono;

public interface ProductManager {

    Mono<Boolean> validateProduct(Product product, Context context);
    Mono<Product> modifyProduct(Product currentProduct, Product newProduct, Context context);
    Mono<Product> enrollProduct(Product product, Context context);

}
