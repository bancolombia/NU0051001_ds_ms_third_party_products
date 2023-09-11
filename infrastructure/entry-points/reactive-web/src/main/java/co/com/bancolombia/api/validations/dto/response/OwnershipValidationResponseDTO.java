package co.com.bancolombia.api.validations.dto.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class OwnershipValidationResponseDTO {

    private OwnershipValidationResponseDTO.OwnershipValidationDataDTO data;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class OwnershipValidationDataDTO {

        private BeneficiaryDTO beneficiary;

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class BeneficiaryDTO {

        private String name;

    }
}
