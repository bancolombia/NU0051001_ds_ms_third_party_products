package co.com.bancolombia.api.validations.mapper;

import co.com.bancolombia.api.validations.dto.response.OwnershipValidationResponseDTO;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ProductValidationMapper {

    public OwnershipValidationResponseDTO mapToOwnershipValidationResponse(String name) {
        return new OwnershipValidationResponseDTO(new OwnershipValidationResponseDTO
                .OwnershipValidationDataDTO(new OwnershipValidationResponseDTO.BeneficiaryDTO(name)));
    }
}