package co.com.bancolombia.api.product.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BeneficiaryDTO(@NotBlank(message = "%BENEFICIARY_NAME_IS_REQUIRED%")
                             @Size(max = 100, message = "%INVALID_BENEFICIARY_NAME_SIZE%") String name,
                             @Valid @NotNull(message = "%REQUIRED_IDENTIFICATION_OBJECT%")
                             IdentificationDTO identification) {
}
