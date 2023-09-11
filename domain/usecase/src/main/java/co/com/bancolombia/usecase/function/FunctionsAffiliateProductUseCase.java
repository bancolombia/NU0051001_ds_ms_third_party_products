package co.com.bancolombia.usecase.function;

import co.com.bancolombia.model.commons.Context;
import co.com.bancolombia.model.commons.Function;
import co.com.bancolombia.model.entity.EntityAdapter;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateway.function.FunctionManager;
import co.com.bancolombia.model.product.gateway.function.FunctionAdapter;
import co.com.bancolombia.usecase.function.utils.FunctionsUseCaseUtils;
import reactor.core.publisher.Mono;
import reactor.function.TupleUtils;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public class FunctionsAffiliateProductUseCase implements FunctionManager {

    private final FunctionAdapter functionAdapter;
    private final EntityAdapter entityAdapter;

    @Override
    public Mono<Set<Function>> enrollProduct(Product product, Context context) {

        return Mono.zip(product.getFunctionList().contains(Function.TRANSFER) ?
                                enrollProductForTransfer(product, context)
                                        .onErrorReturn(Boolean.FALSE)
                                : Mono.just(Boolean.FALSE),
                        FunctionsUseCaseUtils.enrollProductForPayrollPayment(product, context, functionAdapter),
                        FunctionsUseCaseUtils.enrollProductForSupplierPayment(product, context, functionAdapter)
                )
                .flatMap(TupleUtils.function(
                        FunctionsUseCaseUtils::getCompletedProductFunctions
                ));

    }

    @Override
    public Mono<Set<Function>> deleteProduct(Product product, Context context) {
        return Mono.zip(product.getFunctionList().contains(Function.TRANSFER) ?
                                deleteProductForTransfer(product, context)
                                        .onErrorReturn(Boolean.FALSE)
                                : Mono.just(Boolean.FALSE),
                        FunctionsUseCaseUtils.deleteProductForPayrollPayment(product, context, functionAdapter),
                        FunctionsUseCaseUtils.deleteProductForSupplierPayment(product, context, functionAdapter)
                )
                .flatMap(TupleUtils.function(
                        FunctionsUseCaseUtils::getCompletedProductFunctions
                ));
    }

    @Override
    public Mono<Set<Function>> modifyProduct(Product currentProduct, Product newProduct, Context context) {
        var functionsContainer =
                FunctionsUseCaseUtils.getFunctionsToUpdate(newProduct.getFunctionList(),
                        currentProduct.getFunctionList());
        Set<Function> actualFunctions = currentProduct.getFunctionList();
        newProduct.setFunctionList(functionsContainer.functionsToEnroll());
        currentProduct.setFunctionList(functionsContainer.functionsToDelete());
        return FunctionsUseCaseUtils.getUpdatedFunctions(actualFunctions,
                enrollProduct(newProduct, context),
                deleteProduct(currentProduct, context)
        );
    }

    private Mono<Boolean> enrollProductForTransfer(Product product, Context context) {

        return product.getEntity().equals(entityAdapter.getNequiBankCode()) ?
                functionAdapter.enrollProductForTransferNequiAccount(product, context)
                : functionAdapter.enrollProductForTransferOtherBanks(product, context);
    }

    private Mono<Boolean> deleteProductForTransfer(Product product, Context context) {

        return product.getEntity().equals(entityAdapter.getNequiBankCode()) ?
                functionAdapter.deleteProductForTransferNequiAccount(product, context)
                : functionAdapter.deleteProductForTransferOtherBanks(product, context);
    }
}