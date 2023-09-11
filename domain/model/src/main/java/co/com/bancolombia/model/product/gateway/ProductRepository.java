package co.com.bancolombia.model.product.gateway;

import co.com.bancolombia.model.commons.Context;
import co.com.bancolombia.model.commons.DateRange;
import co.com.bancolombia.model.commons.PageRequest;
import co.com.bancolombia.model.commons.PageSummary;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.user.Customer;
import reactor.core.publisher.Mono;

public interface ProductRepository {

    Mono<Product> modifyProduct(Customer customer, Product product);

    Mono<Boolean> isProductRegistered(Product product);
    Mono<Boolean> isCustomerValid(Customer customer);

    Mono<Boolean> isCustomNameAvailable(Customer customer, String customName);

    Mono<Product> enrollProduct(Product product);

    Mono<Product> getProduct(Customer customer, String productId);

    Mono<PageSummary<Product>> searchPaginatedProducts(Product product, PageRequest pageRequest, Context context,
                                                       DateRange dateRange);

    Mono<Boolean> deleteProduct(Customer customer, String productId);

    Mono<Boolean> isValidProductForModification(Product product);
}