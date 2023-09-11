package co.com.bancolombia.api.builders;

import co.com.bancolombia.api.validations.dto.response.OwnershipValidationResponseDTO;
import co.com.bancolombia.builders.BeneficiaryCreator;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ProductValidationTestBuilder {

    public OwnershipValidationResponseDTO buildOwnershipValidationResponseDTO() {
        return new OwnershipValidationResponseDTO(
                new OwnershipValidationResponseDTO.OwnershipValidationDataDTO(
                        new OwnershipValidationResponseDTO.BeneficiaryDTO(BeneficiaryCreator.NAME)));
    }
}