package co.com.bancolombia.usecase.function;

import co.com.bancolombia.model.commons.Context;
import co.com.bancolombia.model.commons.Function;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateway.function.FunctionManager;
import co.com.bancolombia.model.product.gateway.function.FunctionAdapter;
import co.com.bancolombia.usecase.function.utils.FunctionsUseCaseUtils;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.function.TupleUtils;

import java.util.Set;

@RequiredArgsConstructor
public class FunctionsProductUseCase implements FunctionManager {

    private final FunctionAdapter functionAdapter;

    @Override
    public Mono<Set<Function>> enrollProduct(Product product, Context context) {

        return Mono.zip(FunctionsUseCaseUtils.enrollProductForPayrollPayment(product, context, functionAdapter),
                        FunctionsUseCaseUtils.enrollProductForSupplierPayment(product, context, functionAdapter))
                .flatMap(TupleUtils.function((payrollPaymentResult, supplierPaymentResult) ->
                        FunctionsUseCaseUtils.getPayrollAndSupplierPaymentResult(payrollPaymentResult,
                                supplierPaymentResult, product.getFunctionList(),
                                FunctionsUseCaseUtils.ENROLL)
                ));

    }

    @Override
    public Mono<Set<Function>> deleteProduct(Product product, Context context) {

        return Mono.zip(FunctionsUseCaseUtils.deleteProductForPayrollPayment(product, context, functionAdapter),
                        FunctionsUseCaseUtils.deleteProductForSupplierPayment(product, context, functionAdapter))
                .flatMap(TupleUtils.function((payrollPaymentResult, supplierPaymentResult) ->
                        FunctionsUseCaseUtils.getPayrollAndSupplierPaymentResult(payrollPaymentResult,
                                supplierPaymentResult, product.getFunctionList(),
                                FunctionsUseCaseUtils.ENROLL)
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

}
