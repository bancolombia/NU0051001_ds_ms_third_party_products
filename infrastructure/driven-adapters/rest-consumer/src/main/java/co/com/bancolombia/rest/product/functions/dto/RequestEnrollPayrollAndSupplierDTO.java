package co.com.bancolombia.rest.product.functions.dto;

import co.com.bancolombia.rest.commons.dto.IdentificationDTO;
import lombok.NonNull;

public record RequestEnrollPayrollAndSupplierDTO(@NonNull RequestEnrollPayrollAndSupplierPayment data) {

    public record RequestEnrollPayrollAndSupplierPayment(@NonNull PayerDTO payer,
                                                         @NonNull BeneficiaryDTO beneficiary) {
    }

    public record PayerDTO(@NonNull IdentificationDTO identification) {
    }

    public record BeneficiaryDTO(@NonNull String name, @NonNull String paymentType,
                                 @NonNull IdentificationDTO identification, @NonNull AccountDTO account) {
    }

    public record AccountDTO(@NonNull String type, @NonNull String number, @NonNull String bank) {
    }

}