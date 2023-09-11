package co.com.bancolombia.model.product.gateway.function;

import co.com.bancolombia.model.commons.Context;
import co.com.bancolombia.model.product.Product;
import reactor.core.publisher.Mono;

public interface FunctionAdapter {

    Mono<Boolean> enrollProductForPayrollPayment(Product product, Context context);
    Mono<Boolean> enrollProductForSupplierPayment(Product product, Context context);
    Mono<Boolean> deleteProductForPayrollPayment(Product product, Context context);
    Mono<Boolean> deleteProductForSupplierPayment(Product product, Context context);
    Mono<Boolean> enrollProductForTransferOtherBanks(Product product, Context context);
    Mono<Boolean> enrollProductForTransferNequiAccount(Product product, Context context);
    Mono<Boolean> deleteProductForTransferNequiAccount(Product product, Context context);
    Mono<Boolean> deleteProductForTransferOtherBanks(Product product, Context context);

}