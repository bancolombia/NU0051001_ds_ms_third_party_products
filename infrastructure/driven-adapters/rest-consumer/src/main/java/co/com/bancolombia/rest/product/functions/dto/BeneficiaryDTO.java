package co.com.bancolombia.rest.product.functions.dto;

import co.com.bancolombia.rest.commons.dto.IdentificationDTO;
import lombok.NonNull;

public record BeneficiaryDTO(@NonNull IdentificationDTO identification,
                             @NonNull AccountDTO account) {
}