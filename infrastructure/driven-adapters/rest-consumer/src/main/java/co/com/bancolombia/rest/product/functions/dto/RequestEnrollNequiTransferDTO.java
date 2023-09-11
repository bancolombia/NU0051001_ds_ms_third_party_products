package co.com.bancolombia.rest.product.functions.dto;

import co.com.bancolombia.rest.commons.dto.IdentificationDTO;
import lombok.NonNull;

public record RequestEnrollNequiTransferDTO(@NonNull RequestEnrollNequiTransfer data) {

    public record RequestEnrollNequiTransfer(@NonNull EnrollAccountRequestDTO enrollAccountRequest) {
    }

    public record EnrollAccountRequestDTO(@NonNull PayerDTO payer, @NonNull BeneficiaryDTO beneficiary,
                                          @NonNull TransactionDTO transaction) {
    }

    public record PayerDTO(@NonNull IdentificationDTO identification) {
    }

    public record TransactionDTO(@NonNull String channelId) {
    }
}