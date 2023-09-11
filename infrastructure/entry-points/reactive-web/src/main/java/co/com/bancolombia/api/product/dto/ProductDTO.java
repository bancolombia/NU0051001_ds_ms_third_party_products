package co.com.bancolombia.api.product.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProductDTO(@NotBlank(message = "%REQUIRED_PRODUCT_TYPE%")
                         @Size(max = 10, message = "%INVALID_PRODUCT_TYPE_SIZE%") String type,
                         @NotBlank(message = "%REQUIRED_PRODUCT_NUMBER%")
                         @Size(max = 30, message = "%INVALID_PRODUCT_NUMBER_SIZE%")
                         @Digits(fraction = 0, integer = 255, message = "%INVALID_PRODUCT_NUMBER_CONTENT%")
                         String number) {
}
