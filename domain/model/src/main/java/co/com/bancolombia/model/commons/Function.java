package co.com.bancolombia.model.commons;

import co.com.bancolombia.model.exception.BusinessException;
import co.com.bancolombia.model.exception.message.BusinessErrorMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Available Functions to define the product purpose.
 */
@Getter
@RequiredArgsConstructor
public enum Function {

    TRANSFER("transfer"),
    PAYROLL_PAYMENT("payrollPayment"),
    SUPPLIER_PAYMENT("supplierPayment");

    private final String productFunction;

    public static Function findFunction(String content) {
        for (Function function : Function.values()) {
            if (function.getProductFunction().equals(content)) {
                return function;
            }
        }

        throw new BusinessException(BusinessErrorMessage.INCORRECT_PRODUCT_FUNCTION);
    }


}
