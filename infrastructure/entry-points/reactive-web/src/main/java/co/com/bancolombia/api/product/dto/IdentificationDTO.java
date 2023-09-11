package co.com.bancolombia.api.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record IdentificationDTO(@NotBlank(message = "%REQUIRED_IDENTIFICATION_TYPE%")
                                @Size(max = 12, message = "%INVALID_IDENTIFICATION_TYPE_SIZE%") String type,
                                @NotBlank(message = "%REQUIRED_IDENTIFICATION_NUMBER%")
                                @Size(max = 15, message = "%INVALID_IDENTIFICATION_NUMBER_SIZE%") String number) {
}
