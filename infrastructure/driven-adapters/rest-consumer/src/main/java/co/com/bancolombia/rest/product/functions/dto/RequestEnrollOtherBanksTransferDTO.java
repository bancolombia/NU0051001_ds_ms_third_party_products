package co.com.bancolombia.rest.product.functions.dto;

import co.com.bancolombia.rest.commons.dto.IdentificationDTO;
import lombok.NonNull;


public record RequestEnrollOtherBanksTransferDTO(@NonNull RequestEnrollOtherBanksTransfer data) {


    public record RequestEnrollOtherBanksTransfer(@NonNull PayerDTO payer) {
    }

    public record PayerDTO(@NonNull IdentificationDTO identification, @NonNull BeneficiaryDTO beneficiary) {
    }
}