package co.com.bancolombia.api.product.dto.request;


import co.com.bancolombia.api.product.dto.BeneficiaryDTO;
import co.com.bancolombia.api.product.dto.ProductDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;

public record ProductEnrollmentRequestDTO(@NotBlank(message = "%REQUIRED_NAME%")
                                          @Size(max = 60, message = "%INVALID_NAME_SIZE%") String customName,
                                          @NotBlank(message = "%REQUIRED_ENTITY%")
                                          @Size(max = 10, message = "%INVALID_ENTITY_SIZE%") String bankingEntityCode,
                                          @Valid @NotNull(message = "%REQUIRED_PRODUCT_OBJECT%") ProductDTO product,
                                          @Valid @NotNull(message = "%REQUIRED_BENEFICIARY%")
                                          BeneficiaryDTO beneficiary,
                                          @NotNull(message = "%REQUIRED_FUNCTIONS%") Set<FunctionDTO> functions) {
}
