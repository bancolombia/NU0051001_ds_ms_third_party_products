package co.com.bancolombia.usecase.function.utils;

import co.com.bancolombia.model.commons.Context;
import co.com.bancolombia.model.commons.Function;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateway.function.FunctionAdapter;
import lombok.experimental.UtilityClass;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class FunctionsUseCaseUtils {

    public final String ENROLL = "ENROLL";

    public record FunctionsContainer(Set<Function> functionsToEnroll, Set<Function> functionsToDelete) {
    }

    public Mono<Set<Function>> getCompletedProductFunctions(Boolean transferResult,
                                                            Boolean payrollPaymentResult,
                                                            Boolean supplierPaymentResult) {
        Set<Function> completedFunctions = new HashSet<>();

        if (transferResult.equals(Boolean.TRUE)) {
            completedFunctions.add(Function.TRANSFER);
        }
        if (payrollPaymentResult.equals(Boolean.TRUE)) {
            completedFunctions.add(Function.PAYROLL_PAYMENT);
        }
        if (supplierPaymentResult.equals(Boolean.TRUE)) {
            completedFunctions.add(Function.SUPPLIER_PAYMENT);
        }

        return Mono.just(completedFunctions);
    }

    public FunctionsContainer getFunctionsToUpdate(Set<Function> newFunctions, Set<Function> actualFunctions) {

        Set<Function> functionsToEnroll = new HashSet<>(newFunctions);
        functionsToEnroll.removeAll(actualFunctions);

        Set<Function> functionsToDelete = new HashSet<>(actualFunctions);
        functionsToDelete.removeAll(newFunctions);

        return new FunctionsContainer(functionsToEnroll, functionsToDelete);

    }

    public Mono<Set<Function>> getUpdatedFunctions(Set<Function> actualFunctions,
                                                   Mono<Set<Function>> enrolledFunctions,
                                                   Mono<Set<Function>> deletedFunctions) {

        return Mono.zip(Mono.just(actualFunctions), enrolledFunctions, deletedFunctions)
                .flatMap(functionsResults -> {
                    Set<Function> activeFunctions = new HashSet<>(functionsResults.getT1());
                    activeFunctions.addAll(functionsResults.getT2());
                    activeFunctions.removeAll(functionsResults.getT3());
                    return Mono.just(activeFunctions);
                });
    }

    public Mono<Set<Function>> getPayrollAndSupplierPaymentResult(Boolean payrollPaymentResult,
                                                                  Boolean supplierPaymentResult,
                                                                  Set<Function> functions,
                                                                  String actionType) {

        Boolean expectedResultForManagementType = !actionType.equals(ENROLL);
        Set<Function> completedFunctions = new HashSet<>(functions);

        if (payrollPaymentResult.equals(expectedResultForManagementType) &&
                functions.contains(Function.PAYROLL_PAYMENT)) {
            completedFunctions.remove(Function.PAYROLL_PAYMENT);
        }

        if (supplierPaymentResult.equals(expectedResultForManagementType) &&
                functions.contains(Function.SUPPLIER_PAYMENT)) {
            completedFunctions.remove(Function.SUPPLIER_PAYMENT);
        }

        return Mono.just(completedFunctions);
    }

    public Mono<Boolean> enrollProductForPayrollPayment(Product product, Context context,
                                                        FunctionAdapter functionAdapter) {
        return product.getFunctionList().contains(Function.PAYROLL_PAYMENT) ?
                functionAdapter.enrollProductForPayrollPayment(product, context)
                        .onErrorReturn(Boolean.FALSE)
                : Mono.just(Boolean.FALSE);

    }

    public Mono<Boolean> enrollProductForSupplierPayment(Product product, Context context,
                                                         FunctionAdapter functionAdapter) {
        return product.getFunctionList().contains(Function.SUPPLIER_PAYMENT) ?
                functionAdapter.enrollProductForSupplierPayment(product, context)
                        .onErrorReturn(Boolean.FALSE)
                : Mono.just(Boolean.FALSE);
    }

    public Mono<Boolean> deleteProductForPayrollPayment(Product product, Context context,
                                                        FunctionAdapter functionAdapter) {
        return product.getFunctionList().contains(Function.PAYROLL_PAYMENT) ?
                functionAdapter.deleteProductForPayrollPayment(product, context)
                        .onErrorReturn(Boolean.FALSE)
                : Mono.just(Boolean.FALSE);
    }

    public Mono<Boolean> deleteProductForSupplierPayment(Product product, Context context,
                                                         FunctionAdapter functionAdapter) {
        return product.getFunctionList().contains(Function.SUPPLIER_PAYMENT) ?
                functionAdapter.deleteProductForSupplierPayment(product, context)
                        .onErrorReturn(Boolean.FALSE)
                : Mono.just(Boolean.FALSE);
    }

}
